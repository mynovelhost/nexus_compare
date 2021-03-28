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
package org.sonatype.nexus.repository.browse.node;

import javax.inject.Inject;
import javax.inject.Named;

import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.RepositoryTaskSupport;
import org.sonatype.nexus.scheduling.Cancelable;

/**
 * Browse nodes rebuild task.
 *
 * @since 3.6
 */
@Named
public class RebuildBrowseNodesTask
    extends RepositoryTaskSupport
    implements Cancelable
{
  private final RebuildBrowseNodeService rebuildBrowseNodeService;

  @Inject
  public RebuildBrowseNodesTask(final RebuildBrowseNodeService rebuildBrowseNodeService)
  {
    this.rebuildBrowseNodeService = rebuildBrowseNodeService;
  }

  @Override
  public String getMessage() {
    return "Rebuilding browse tree for " + getRepositoryField();
  }

  @Override
  protected void execute(final Repository repo) {
    try {
      rebuildBrowseNodeService.rebuild(repo, this::isCanceled);
    }
    catch (RebuildBrowseNodeFailedException e) {
      log.error("Error rebuild browse nodes for repository: {}", repo, e);
    }
  }

  @Override
  protected boolean appliesTo(final Repository repository) {
    return repository != null;
  }
}
