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
package org.sonatype.nexus.repository.pypi.internal.orient;

import javax.inject.Named;

import org.sonatype.nexus.repository.FacetSupport;
import org.sonatype.nexus.repository.pypi.internal.PyPiIndexFacet;
import org.sonatype.nexus.repository.pypi.internal.PyPiPathUtils;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.Bucket;
import org.sonatype.nexus.repository.storage.StorageTx;
import org.sonatype.nexus.repository.transaction.TransactionalDeleteBlob;
import org.sonatype.nexus.transaction.UnitOfWork;

import static org.sonatype.nexus.repository.pypi.internal.PyPiPathUtils.INDEX_PATH_PREFIX;
import static org.sonatype.nexus.repository.pypi.internal.PyPiPathUtils.normalizeName;
import static org.sonatype.nexus.repository.pypi.internal.orient.OrientPyPiDataUtils.findAsset;

/**
 * {@link PyPiIndexFacet} implementation.
 *
 * @since 3.16
 */
@Named
public class OrientPyPiIndexFacetImpl
    extends FacetSupport
    implements PyPiIndexFacet
{
  @TransactionalDeleteBlob
  public void deleteIndex(final String packageName)
  {
    StorageTx tx = UnitOfWork.currentTx();
    Bucket bucket = tx.findBucket(getRepository());

    String indexPath = PyPiPathUtils.indexPath(normalizeName(packageName));
    Asset cachedIndex = findAsset(tx, bucket, indexPath);
    
    /*
      There is a chance that the index wasn't found because of package name normalization. For example '.' 
      characters are normalized to '-' so jts.python would have an index at /simple/jts-python/. It is possible that 
      we could just check for the normalized name but we check for both just in case. Searching for an index with a
      normalized name first means that most, if not all, index deletions will only perform a single search.
      
      See https://issues.sonatype.org/browse/NEXUS-19303 for additional context. 
     */
    if (cachedIndex == null) {
      indexPath = PyPiPathUtils.indexPath(packageName);
      cachedIndex = findAsset(tx, bucket, indexPath);
    }
    
    if (cachedIndex != null) {
      tx.deleteAsset(cachedIndex);
    }
  }

  @TransactionalDeleteBlob
  public void deleteRootIndex() {
    StorageTx tx = UnitOfWork.currentTx();
    Bucket bucket = tx.findBucket(getRepository());
    Asset rootIndex = findAsset(tx, bucket, INDEX_PATH_PREFIX);
    if (rootIndex != null) {
      tx.deleteAsset(rootIndex);
    }
  }
}
