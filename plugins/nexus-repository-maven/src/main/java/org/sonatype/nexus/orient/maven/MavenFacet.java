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
package org.sonatype.nexus.orient.maven;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.sonatype.nexus.common.collect.AttributesMap;
import org.sonatype.nexus.common.hash.HashAlgorithm;
import org.sonatype.nexus.repository.Facet;
import org.sonatype.nexus.repository.maven.LayoutPolicy;
import org.sonatype.nexus.repository.maven.MavenPath;
import org.sonatype.nexus.repository.maven.MavenPathParser;
import org.sonatype.nexus.repository.maven.VersionPolicy;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.AssetBlob;
import org.sonatype.nexus.repository.storage.Bucket;
import org.sonatype.nexus.repository.storage.TempBlob;
import org.sonatype.nexus.repository.view.Content;
import org.sonatype.nexus.repository.view.Payload;

import com.google.common.hash.HashCode;

/**
 * Maven facet, present on all Maven repositories.
 *
 * @since 3.0
 */
@Facet.Exposed
public interface MavenFacet
    extends Facet
{
  /**
   * Returns the format specific {@link MavenPathParser}.
   */
  @Nonnull
  MavenPathParser getMavenPathParser();

  /**
   * Returns the version policy in effect for this repository.
   */
  @Nonnull
  VersionPolicy getVersionPolicy();

  /**
   * Returns the layout policy in effect for this repository.
   */
  LayoutPolicy layoutPolicy();

  // HTTP operations

  @Nullable
  Content get(MavenPath path) throws IOException;

  Content put(MavenPath path, Payload payload) throws IOException;

  Content put(MavenPath path,
              Path sourceFile,
              String contentType,
              AttributesMap contentAttributes,
              Map<HashAlgorithm, HashCode> hashes,
              long size) throws IOException;

  /**
   * Puts an artifact held in a temporary blob.
   * @since 3.1
   */
  Content put(MavenPath path, TempBlob blob, String contentType, AttributesMap contentAttributes) throws IOException;

  boolean delete(MavenPath... paths) throws IOException;

  /**
   * @since 3.4
   */
  Asset put(MavenPath path, AssetBlob assetBlob, AttributesMap contentAttributes) throws IOException;

  /**
   * @since 3.14
   *
   * @param path of the asset to check
   * @return true if it exists
   */
  boolean exists(final MavenPath path);

  /**
   * @since 3.24
   * @param path to the maven component that might need metadata rebuilt or deleted
   */
  void maybeDeleteOrFlagToRebuildMetadata(final Bucket bucket, final MavenPath path) throws IOException;

  /**
   * @since 3.24
   * @param paths to the maven components that might need metadata rebuilt or deleted
   */
  void maybeDeleteOrFlagToRebuildMetadata(final Collection<MavenPath> paths) throws IOException;
}
