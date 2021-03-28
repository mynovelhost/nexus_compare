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
package org.sonatype.nexus.internal.scheduling

import javax.inject.Named
import javax.inject.Singleton

import org.sonatype.nexus.security.config.MemorySecurityConfiguration
import org.sonatype.nexus.security.config.SecurityContributor
import org.sonatype.nexus.security.config.memory.MemoryCPrivilege

/**
 * Scheduling security configuration.
 *
 * @since 3.0
 */
@Named
@Singleton
class SchedulingSecurityContributor
    implements SecurityContributor
{
  @Override
  MemorySecurityConfiguration getContribution() {
    return new MemorySecurityConfiguration(
        privileges: [
            new MemoryCPrivilege(
                id: 'nx-tasks-all',
                description: 'All permissions for Scheduled Tasks',
                type: 'application',
                properties: [
                    domain : 'tasks',
                    actions: '*'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-tasks-create',
                description: 'Create permission for Scheduled Tasks',
                type: 'application',
                properties: [
                    domain : 'tasks',
                    actions: 'create,read'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-tasks-read',
                description: 'Read permission for Scheduled Tasks',
                type: 'application',
                properties: [
                    domain : 'tasks',
                    actions: 'read'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-tasks-update',
                description: 'Update permission for Scheduled Tasks',
                type: 'application',
                properties: [
                    domain : 'tasks',
                    actions: 'update,read'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-tasks-delete',
                description: 'Delete permission for Scheduled Tasks',
                type: 'application',
                properties: [
                    domain : 'tasks',
                    actions: 'delete,read'
                ]
            ),

            new MemoryCPrivilege(
                id: 'nx-tasks-run',
                description: 'Run permission for Scheduled Tasks',
                type: 'application',
                properties: [
                    domain : 'tasks',
                    actions: 'start,stop'
                ]
            )
        ]
    )
  }
}

