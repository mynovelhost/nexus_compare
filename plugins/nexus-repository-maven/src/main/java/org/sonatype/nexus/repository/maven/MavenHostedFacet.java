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
package org.sonatype.nexus.repository.maven;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.sonatype.nexus.repository.Facet;

/**
 * Maven hosted facet, present on all Maven hosted-type repositories.
 *
 * @since 3.0
 */
@Facet.Exposed
public interface MavenHostedFacet
    extends Facet
{
  /**
   * Rebuilds/updates Maven metadata. The parameters depend each on previous, and if any of those are set (ie. G, GA or
   * GAV), the metadata will be updated. Rebuild is possible only against repository as whole, not a sub-part of it.
   *
   * @param groupId     scope the work to given groupId.
   * @param artifactId  scope the work to given artifactId (groupId must be given).
   * @param baseVersion scope the work to given baseVersion (groupId and artifactId must be given).
   * @param rebuildChecksums  whether or not checksums should be checked and corrected if found                     
   *                           missing or incorrect 
   */
  void rebuildMetadata(@Nullable String groupId,
                       @Nullable String artifactId,
                       @Nullable String baseVersion,
                       boolean rebuildChecksums);

  /**
   * Rebuilds/updates Maven metadata. The parameters depend each on previous, and if any of those are set (ie. G, GA or
   * GAV), the metadata will be updated. Rebuild is possible only against repository as whole, not a sub-part of it.
   *
   * @param groupId     scope the work to given groupId.
   * @param artifactId  scope the work to given artifactId (groupId must be given).
   * @param baseVersion scope the work to given baseVersion (groupId and artifactId must be given).
   * @param rebuildChecksums  whether or not checksums should be checked and corrected if found
   *                           missing or incorrect
   * @param update      whether to update or replace metadata
   *
   * @since 3.22
   */
  void rebuildMetadata(@Nullable String groupId,
                       @Nullable String artifactId,
                       @Nullable String baseVersion,
                       boolean rebuildChecksums,
                       boolean update);

  /**
   * Rebuilds archetype catalog for given repository. Returns the number of archetypes hosted.
   */
  int rebuildArchetypeCatalog() throws IOException;

  /**
   * Delete metadata associated with the Maven artifact, and rebuild metadata to account for the deletion.
   */
  Set<String> deleteMetadata(String groupId, String artifactId, String baseVersion);

  /**
   * Delete metadata associated with the Maven artifact, and rebuild metadata to account for the deletion.
   */
  void deleteMetadata(List<String[]> gavs);
}
