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
import React from 'react';
import {useMachine} from '@xstate/react';
import UIStrings from '../../../../constants/UIStrings';
import {ContentBody, ExtJS, NxLoadWrapper, Section} from "nexus-ui-plugin";

import SupportZipForm from "./SupportZipForm";
import SupportZipResponse from "./SupportZipResponse";
import SupportZipResponseHA from "./SupportZipResponseHA";
import SupportZipMachine from "./SupportZipMachine";

export default function SupportZip() {
  const [current, send] = useMachine(SupportZipMachine, {devTools: true});
  const isLoading = current.matches('creatingSupportZips') || current.matches('creatingHaSupportZips');
  const isCreated = current.matches('supportZipsCreated');
  const isHaZipsCreated = current.matches('haSupportZipsCreated');
  const {params, response, createError} = current.context;

  function setParams({target}) {
    send('UPDATE', {
      params: {
        ...params,
        [target.id]: target.checked
      }
    });
  }

  function submit(event) {
    event.preventDefault();
    send('CREATE_SUPPORT_ZIPS');
  }

  function hazips(event) {
    event.preventDefault();
    send('CREATE_HA_SUPPORT_ZIPS');
  }

  function download(event, filename) {
    event.preventDefault();
    ExtJS.requestAuthenticationToken(UIStrings.SUPPORT_ZIP.AUTHENTICATE_TEXT).then(authToken => {
      const url = ExtJS.urlOf(`service/rest/wonderland/download/${filename}?t=${btoa(authToken)}`);
      ExtJS.downloadUrl(url);
    });
  }

  return <ContentBody className="nxrm-support-zip">
    <Section>
      {!(isCreated || isHaZipsCreated) &&
        <NxLoadWrapper loading={isLoading} error={createError ? `${createError}` : null}>
          <SupportZipForm params={params} setParams={setParams} submit={submit} clustered={ExtJS.state().isClustered()} hazips={hazips}/>
        </NxLoadWrapper>
      }
      {isCreated && <SupportZipResponse response={response} download={download}/>}
      {isHaZipsCreated && <SupportZipResponseHA response={response} download={download}/>}
    </Section>
  </ContentBody>;
}
