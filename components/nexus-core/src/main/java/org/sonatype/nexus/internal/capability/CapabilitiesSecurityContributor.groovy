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
package org.sonatype.nexus.internal.capability

import javax.inject.Named
import javax.inject.Singleton

import org.sonatype.nexus.security.config.MemorySecurityConfiguration
import org.sonatype.nexus.security.config.SecurityContributor
import org.sonatype.nexus.security.config.memory.MemoryCPrivilege

// FIXME: normalize names to use capability instead of capabilities

/**
 * Capabilities security configuration.
 *
 * @since 3.0
 */
@Named
@Singleton
class CapabilitiesSecurityContributor
    implements SecurityContributor
{
  @Override
  MemorySecurityConfiguration getContribution() {
    return new MemorySecurityConfiguration(
        privileges: [
            new MemoryCPrivilege(
                id: 'nx-capabilities-all',
                description: 'All permissions for Capabilities',
                type: 'application',
                properties: [
                    domain : 'capabilities',
                    actions: '*'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-capabilities-create',
                description: 'Create permission for Capabilities',
                type: 'application',
                properties: [
                    domain : 'capabilities',
                    actions: 'create,read'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-capabilities-read',
                description: 'Read permission for Capabilities',
                type: 'application',
                properties: [
                    domain : 'capabilities',
                    actions: 'read'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-capabilities-update',
                description: 'Update permission for Capabilities',
                type: 'application',
                properties: [
                    domain : 'capabilities',
                    actions: 'update,read'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-capabilities-delete',
                description: 'Delete permission for Capabilities',
                type: 'application',
                properties: [
                    domain : 'capabilities',
                    actions: 'delete,read'
                ]
            )
        ]
    )
  }
}

