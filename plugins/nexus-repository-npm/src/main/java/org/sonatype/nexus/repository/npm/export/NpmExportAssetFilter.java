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
package org.sonatype.nexus.repository.npm.export;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.repository.filter.export.ExportAssetFilter;
import org.sonatype.nexus.repository.npm.internal.NpmAttributes.AssetKind;
import org.sonatype.nexus.repository.npm.internal.NpmFormat;
import org.sonatype.nexus.repository.storage.Asset;

import static org.sonatype.nexus.repository.storage.AssetEntityAdapter.P_ASSET_KIND;

/**
 * @since 3.25.0
 */
@Singleton
@Named(NpmFormat.NAME)
public class NpmExportAssetFilter implements ExportAssetFilter
{
  @Override
  public boolean shouldSkipAsset(final Asset asset) {
    return (!asset.name().endsWith(".tgz"));
  }
}
