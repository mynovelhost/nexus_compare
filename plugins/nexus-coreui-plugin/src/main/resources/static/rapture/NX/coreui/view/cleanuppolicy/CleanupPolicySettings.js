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
/*global Ext, NX*/

/**
 * Cleanup Policy "Settings" panel.
 *
 * @since 3.14
 */
Ext.define('NX.coreui.view.cleanuppolicy.CleanupPolicySettings', {
  extend: 'NX.view.SettingsPanel',
  alias: 'widget.nx-coreui-cleanuppolicy-settings',
  requires: [
    'NX.I18n'
  ],

  /**
   * @override
   */
  initComponent: function() {
    Ext.apply(this, {
      title: NX.I18n.get('CleanupPolicy_CleanupPolicyFeature_Settings_Title'),
      settingsForm: {
        xtype: 'nx-coreui-cleanuppolicy-settings-form',
        buttons: [
          {
            text: NX.I18n.get('CleanupPolicy_CleanupPolicyList_Preview_Button'),
            action: 'preview',
            ui: 'nx-primary'
          },
          {text: NX.I18n.get('SettingsForm_Save_Button'), action: 'save', ui: 'nx-primary', bindToEnter: false},
          {text: NX.I18n.get('SettingsForm_Discard_Button'), action: 'discard'}
        ]
      },
      dockedItems: null
    });

    this.callParent();
  }
});
