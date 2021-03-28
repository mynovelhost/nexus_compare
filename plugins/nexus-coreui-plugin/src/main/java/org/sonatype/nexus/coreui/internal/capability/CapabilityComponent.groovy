/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.coreui.internal.capability

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.groups.Default

import org.sonatype.nexus.capability.Capability
import org.sonatype.nexus.capability.CapabilityDescriptor
import org.sonatype.nexus.capability.CapabilityDescriptorRegistry
import org.sonatype.nexus.capability.CapabilityReference
import org.sonatype.nexus.capability.CapabilityReferenceFilterBuilder.CapabilityReferenceFilter
import org.sonatype.nexus.capability.CapabilityRegistry
import org.sonatype.nexus.capability.Tag
import org.sonatype.nexus.capability.Taggable
import org.sonatype.nexus.coreui.FormFieldXO
import org.sonatype.nexus.extdirect.DirectComponent
import org.sonatype.nexus.extdirect.DirectComponentSupport
import org.sonatype.nexus.rapture.PasswordPlaceholder
import org.sonatype.nexus.rapture.StateContributor
import org.sonatype.nexus.validation.Validate
import org.sonatype.nexus.validation.group.Create
import org.sonatype.nexus.validation.group.Update

import com.codahale.metrics.annotation.ExceptionMetered
import com.codahale.metrics.annotation.Timed
import com.softwarementors.extjs.djn.config.annotations.DirectAction
import com.softwarementors.extjs.djn.config.annotations.DirectMethod
import groovy.transform.PackageScope
import org.apache.shiro.authz.annotation.RequiresAuthentication
import org.apache.shiro.authz.annotation.RequiresPermissions
import org.hibernate.validator.constraints.NotEmpty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.sonatype.nexus.capability.CapabilityIdentity.capabilityIdentity
import static org.sonatype.nexus.capability.CapabilityReferenceFilterBuilder.capabilities
import static org.sonatype.nexus.capability.CapabilityType.capabilityType

// FIXME: update action name after refactor to use coreui_*

/**
 * Capabilities {@link DirectComponent}.
 *
 * @since 3.0
 */
@Named
@Singleton
@DirectAction(action = 'capability_Capability')
class CapabilityComponent
    extends DirectComponentSupport
    implements StateContributor
{

  private static final Logger log = LoggerFactory.getLogger(CapabilityComponent.class)

  private static final CapabilityReferenceFilter ALL_CREATED = capabilities().includeNotExposed()

  private static final CapabilityReferenceFilter ALL_ACTIVE = capabilities().includeNotExposed().active()

  @Inject
  private CapabilityDescriptorRegistry capabilityDescriptorRegistry

  @Inject
  private CapabilityRegistry capabilityRegistry

  /**
   * Retrieves capabilities.
   * @return a list of capabilities
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresPermissions('nexus:capabilities:read')
  List<CapabilityXO> read() {
    return capabilityRegistry.get(capabilities()).collect { capability ->
      asCapability(capability)
    }
  }

  /**
   * Retrieve available capabilities types.
   * @return a list of capability types
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresPermissions('nexus:capabilities:read')
  List<CapabilityTypeXO> readTypes() {
    return capabilityDescriptorRegistry.all.findAll { it.exposed }.collect { descriptor ->
      new CapabilityTypeXO(
          id: descriptor.type(),
          name: descriptor.name(),
          about: descriptor.about(),
          formFields: descriptor.formFields()?.collect { FormFieldXO.create(it) }
      )
    }
  }

  /**
   * Creates a capability.
   * @param capabilityXO to be created
   * @return created capability
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresAuthentication
  @RequiresPermissions('nexus:capabilities:create')
  @Validate(groups = [Create.class, Default.class])
  CapabilityXO create(final @NotNull @Valid CapabilityXO capabilityXO) {
    return asCapability(capabilityRegistry.add(
        capabilityType(capabilityXO.typeId),
        capabilityXO.enabled,
        capabilityXO.notes,
        capabilityXO.properties
    ))
  }

  /**
   * Updates a capability.
   * @param capabilityXO to be updated
   * @return updated capability
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresAuthentication
  @RequiresPermissions('nexus:capabilities:update')
  @Validate(groups = [Update.class, Default.class])
  CapabilityXO update(final @NotNull @Valid CapabilityXO capabilityXO) {
    def reference = capabilityRegistry.get(capabilityIdentity(capabilityXO.id))
    return asCapability(capabilityRegistry.update(
        capabilityIdentity(capabilityXO.id),
        capabilityXO.enabled,
        capabilityXO.notes,
        unfilterProperties(capabilityXO.properties, reference.context().properties())
    ))
  }

  /**
   * Updates capability notes.
   * @param capabilityNotesXO to be updated
   * @return updated capability
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresAuthentication
  @RequiresPermissions('nexus:capabilities:update')
  @Validate(groups = [Update.class, Default.class])
  CapabilityXO updateNotes(final @NotNull @Valid CapabilityNotesXO capabilityNotesXO) {
    def reference = capabilityRegistry.get(capabilityIdentity(capabilityNotesXO.id))
    return asCapability(capabilityRegistry.update(
        reference.context().id(),
        reference.context().enabled,
        capabilityNotesXO.notes,
        reference.context().properties()
    ))
  }

  /**
   * Deletes a capability.
   * @param id of capability to be deleted
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresAuthentication
  @RequiresPermissions('nexus:capabilities:delete')
  @Validate
  void remove(final @NotEmpty String id) {
    capabilityRegistry.remove(capabilityIdentity(id))
  }

  /**
   * Enables an existing capability.
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresAuthentication
  @RequiresPermissions('nexus:capabilities:update')
  @Validate
  void enable(final @NotEmpty String id) {
    capabilityRegistry.enable(capabilityIdentity(id))
  }

  /**
   * Disables an existing capability.
   */
  @DirectMethod
  @Timed
  @ExceptionMetered
  @RequiresAuthentication
  @RequiresPermissions('nexus:capabilities:update')
  @Validate
  void disable(final @NotEmpty String id) {
    capabilityRegistry.disable(capabilityIdentity(id))
  }

  @Override
  Map<String, Object> getState() {
    def createdTypes = capabilityRegistry.get(ALL_CREATED).collect(capabilityToType) as Set
    def activeTypes = capabilityRegistry.get(ALL_ACTIVE).collect(capabilityToType) as Set
    return [
        capabilityCreatedTypes: createdTypes,
        capabilityActiveTypes: activeTypes
    ]
  }

  private capabilityToType = { CapabilityReference capability -> capability.context().descriptor().type().toString() }

  @PackageScope
  CapabilityXO asCapability(final CapabilityReference reference) {
    CapabilityDescriptor descriptor = reference.context().descriptor()
    Capability capability = reference.capability()

    CapabilityXO capabilityXO = new CapabilityXO(
        id: reference.context().id(),
        notes: reference.context().notes(),
        typeId: descriptor.type(),
        typeName: descriptor.name(),
        enabled: reference.context().enabled,
        active: reference.context().active,
        error: reference.context().hasFailure(),
        state: 'disabled',
        stateDescription: reference.context().stateDescription(),
        properties: filterProperties(reference.context().properties(), capability)
    )

    if (capabilityXO.enabled && capabilityXO.error) {
      capabilityXO.state = 'error'
    }
    else if (capabilityXO.enabled && capabilityXO.active) {
      capabilityXO.state = 'active'
    }
    else if (capabilityXO.enabled && !capabilityXO.active) {
      capabilityXO.state = 'passive'
    }

    try {
      capabilityXO.description = capability.description()
    }
    catch (Exception e) {
      log.debug('Failed to retrieve description from capability {}', descriptor, e)
    }

    try {
      capabilityXO.status = capability.status()
    }
    catch (Exception e) {
      log.debug('Failed to retrieve status from capability {}', descriptor, e)
    }

    Set<Tag> tags = [] as Set
    try {
      if (descriptor instanceof Taggable) {
        descriptor.tags?.with { tags.addAll(it) }
      }
    }
    catch (Exception e) {
      log.debug('Failed to retrieve tags from capability descriptor {}', descriptor, e)
    }
    try {
      if (capability instanceof Taggable) {
        capability.tags?.with { tags.addAll(it) }
      }
    }
    catch (Exception e) {
      log.debug('Failed to retrieve tags from capability {}', descriptor, e)
    }

    if (!tags.empty) {
      capabilityXO.tags = tags.collectEntries { tag -> [tag.key(), tag.value()] }
    }

    return capabilityXO
  }

  private Map<String, String> filterProperties(final Map<String, String> properties, final Capability capability) {
    properties.collectEntries { key, value ->
      if (capability.isPasswordProperty(key)) {
        if ('PKI'.equals(properties.get('authenticationType'))) {
          [key, '']
        }
        else {
          [key, PasswordPlaceholder.get()]
        }
      }
      else {
        [key, value]
      }
    }
  }

  private Map<String, String> unfilterProperties(final Map<String, String> properties,
                                                 final Map<String, String> referenceProperties) {
    properties.collectEntries { key, value ->
      if (PasswordPlaceholder.is(value)) {
        [key, referenceProperties[key]]
      }
      else {
        [key, value]
      }
    }
  }
}
