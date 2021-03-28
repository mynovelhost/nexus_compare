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
package org.sonatype.nexus.repository.internal

import javax.inject.Named
import javax.inject.Singleton

import org.sonatype.nexus.security.config.MemorySecurityConfiguration
import org.sonatype.nexus.security.config.SecurityContributor
import org.sonatype.nexus.security.config.memory.MemoryCPrivilege

/**
 * Repository view security configuration.
 *
 * @since 3.0
 */
@Named
@Singleton
class RepositoryViewSecurityContributor
    implements SecurityContributor
{
  @Override
  MemorySecurityConfiguration getContribution() {
    return new MemorySecurityConfiguration(
        privileges: [
            //
            // nexus:repository-view
            //

            new MemoryCPrivilege(
                id: 'nx-repository-view-*-*-*',
                description: 'All permissions for all repository views',
                type: 'repository-view',
                properties: [
                    format: '*',
                    repository: '*',
                    actions: '*'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-repository-view-*-*-browse',
                description: 'Browse permission for all repository views',
                type: 'repository-view',
                properties: [
                    format: '*',
                    repository: '*',
                    actions: 'browse'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-repository-view-*-*-read',
                description: 'Read permission for all repository views',
                type: 'repository-view',
                properties: [
                    format: '*',
                    repository: '*',
                    actions: 'read'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-repository-view-*-*-edit',
                description: 'Edit permission for all repository views',
                type: 'repository-view',
                properties: [
                    format: '*',
                    repository: '*',
                    actions: 'edit'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-repository-view-*-*-add',
                description: 'Add permission for all repository views',
                type: 'repository-view',
                properties: [
                    format: '*',
                    repository: '*',
                    actions: 'add'
                ]
            ),
            new MemoryCPrivilege(
                id: 'nx-repository-view-*-*-delete',
                description: 'Delete permission for all repository views',
                type: 'repository-view',
                properties: [
                    format: '*',
                    repository: '*',
                    actions: 'delete'
                ]
            ),
        ]
    )
  }
}

