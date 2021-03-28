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
package org.sonatype.nexus.repository.content.fluent.internal;

import java.util.Optional;

import org.sonatype.nexus.repository.content.Component;
import org.sonatype.nexus.repository.content.facet.ContentFacetSupport;
import org.sonatype.nexus.repository.content.fluent.FluentComponent;
import org.sonatype.nexus.repository.content.fluent.FluentComponentBuilder;
import org.sonatype.nexus.repository.content.store.ComponentData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link FluentComponentBuilder} implementation.
 *
 * @since 3.24
 */
public class FluentComponentBuilderImpl
    implements FluentComponentBuilder
{
  private final ContentFacetSupport facet;

  private final String name;

  private String kind = "";

  private String namespace = "";

  private String version = "";

  public FluentComponentBuilderImpl(final ContentFacetSupport facet, final String name) {
    this.facet = checkNotNull(facet);
    this.name = checkNotNull(name);
  }

  @Override
  public FluentComponentBuilder namespace(final String namespace) {
    this.namespace = checkNotNull(namespace);
    return this;
  }

  @Override
  public FluentComponentBuilder kind(String kind) {
    this.kind = checkNotNull(kind);
    return this;
  }

  @Override
  public FluentComponentBuilder version(final String version) {
    this.version = checkNotNull(version);
    return this;
  }

  @Override
  public FluentComponent getOrCreate() {
    return new FluentComponentImpl(facet, facet.stores().componentStore
        .readComponent(facet.contentRepositoryId(), namespace, name, version)
        .orElseGet(this::createComponent));
  }

  @Override
  public Optional<FluentComponent> find() {
    return facet.stores().componentStore
        .readComponent(facet.contentRepositoryId(), namespace, name, version)
        .map(component -> new FluentComponentImpl(facet, component));
  }

  private Component createComponent() {
    ComponentData component = new ComponentData();
    component.setRepositoryId(facet.contentRepositoryId());
    component.setNamespace(namespace);
    component.setName(name);
    component.setKind(kind);
    component.setVersion(version);

    facet.stores().componentStore.createComponent(component);

    return component;
  }
}
