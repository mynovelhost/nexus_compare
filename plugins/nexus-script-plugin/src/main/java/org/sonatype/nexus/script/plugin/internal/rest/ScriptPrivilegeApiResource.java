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
package org.sonatype.nexus.script.plugin.internal.rest;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sonatype.nexus.rest.Resource;
import org.sonatype.nexus.script.plugin.internal.security.ScriptPrivilegeDescriptor;
import org.sonatype.nexus.security.SecuritySystem;
import org.sonatype.nexus.security.internal.rest.SecurityApiResource;
import org.sonatype.nexus.security.privilege.PrivilegeDescriptor;
import org.sonatype.nexus.security.privilege.rest.PrivilegeApiResourceSupport;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @since 3.19
 */
@Named
@Singleton
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path(ScriptPrivilegeApiResource.RESOURCE_URI)
public class ScriptPrivilegeApiResource
    extends PrivilegeApiResourceSupport
    implements Resource, ScriptPrivilegeApiResourceDoc
{
  public static final String RESOURCE_URI = SecurityApiResource.RESOURCE_URI + "privileges";

  @Inject
  public ScriptPrivilegeApiResource(final SecuritySystem securitySystem,
                                    final Map<String, PrivilegeDescriptor> privilegeDescriptors)
  {
    super(securitySystem, privilegeDescriptors);
  }

  @Override
  @POST
  @RequiresAuthentication
  @RequiresPermissions("nexus:privileges:create")
  @Path("script")
  public Response createPrivilege(final ApiPrivilegeScriptRequest privilege) {
    return doCreate(ScriptPrivilegeDescriptor.TYPE, privilege);
  }

  @Override
  @PUT
  @RequiresAuthentication
  @RequiresPermissions("nexus:privileges:update")
  @Path("script/{privilegeId}")
  public void updatePrivilege(@PathParam("privilegeId") final String privilegeId,
                              final ApiPrivilegeScriptRequest privilege)
  {
    doUpdate(privilegeId, ScriptPrivilegeDescriptor.TYPE, privilege);
  }
}
