/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.cnd.debugger.dbx.breakpoints.types;

import org.netbeans.modules.cnd.debugger.common2.debugger.breakpoints.NativeBreakpoint;
import org.netbeans.modules.cnd.debugger.common2.debugger.breakpoints.BreakpointPanel;
import org.netbeans.modules.cnd.debugger.common2.debugger.breakpoints.SystemInfo;
import java.util.Vector;
import java.awt.Component;

import org.netbeans.modules.cnd.debugger.common2.utils.IpeUtils;
import org.netbeans.modules.cnd.debugger.common2.values.SysCallEE;

/**
 * @deprecated Use the same class from common instead
 */
@Deprecated
class SysCallBreakpointPanel extends BreakpointPanel {

    private SysCallBreakpoint fb;
    private SystemInfo si_syscalls;
    
    public void seed(NativeBreakpoint breakpoint) {
	seedCommonComponents(breakpoint);
	fb = (SysCallBreakpoint) breakpoint;

	// LATER
	// The following doesn't work as explained in SignalBreakpointType

	if (fb.getSysCall() == null)
	    sysCallCombo.setSelectedItem(si_syscalls.all());
	else
	    sysCallCombo.setSelectedItem(fb.getSysCall());

	if (fb.getEntryExit() == SysCallEE.ENTRY) 
	    entranceToggle.setSelected(true);
	else
	    exitToggle.setSelected(true);
    }

    /*
     * Constructors
     */
    public SysCallBreakpointPanel() {
	this (new SysCallBreakpoint(NativeBreakpoint.TOPLEVEL), false);
    }

    public SysCallBreakpointPanel(NativeBreakpoint b) {
	this ((SysCallBreakpoint)b, true);
    }

    /** Creates new form SysCallBreakpointPanel */
    public SysCallBreakpointPanel(SysCallBreakpoint breakpoint,
				  boolean customizing) {
	super(breakpoint, customizing);
	fb = breakpoint;

	initComponents();
	addCommonComponents(3);
	entranceToggle.setSelected(true);

	sysCallCombo.setEditable(true);
	Component c = sysCallCombo.getEditor().getEditorComponent();
	if (c instanceof javax.swing.text.JTextComponent) {
	    javax.swing.text.Document d =
		((javax.swing.text.JTextComponent)c).getDocument();
	    d.addDocumentListener(this);
	}

	// Fill in actual syscalls asynchronously like this:
	/** Items in the combo boxes */
	Vector<String> comboValues = new Vector<String>(250);
	si_syscalls = new SystemInfo.Syscalls(comboValues);
	si_syscalls.stuffInto(sysCallCombo);

	seed(breakpoint);

	// Arrange to revalidate on changes
	sysCallCombo.addItemListener(this);
    }
    
    public void setDescriptionEnabled(boolean enabled) {
	// sysCallLabel.setEnabled(false);
	sysCallCombo.setEnabled(false);
	entranceToggle.setEnabled(false);
	exitToggle.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
	buttonGroup1 = new javax.swing.ButtonGroup();
	sysCallLabel = new javax.swing.JLabel();
	sysCallCombo = new javax.swing.JComboBox();
	entranceToggle = new javax.swing.JRadioButton();
	exitToggle = new javax.swing.JRadioButton();

	panel_settings.setLayout(new java.awt.GridBagLayout());
	java.awt.GridBagConstraints gridBagConstraints1;

	sysCallLabel.setText(Catalog.get("SystemCall"));// NOI18N
	sysCallLabel.setDisplayedMnemonic(
	    Catalog.getMnemonic("MNEM_SystemCall"));	// NOI18N
	sysCallLabel.setLabelFor(sysCallCombo);
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.ipadx = 5;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	panel_settings.add(sysCallLabel, gridBagConstraints1);

	sysCallCombo.setEditable(true);
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridwidth = 3;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.weightx = 1.0;
	panel_settings.add(sysCallCombo, gridBagConstraints1);

	entranceToggle.setText(SysCallEE.ENTRY.toString());	// NOI18N
	entranceToggle.setMnemonic(
	    Catalog.getMnemonic("MNEM_OnEntrance"));		// NOI18N
	buttonGroup1.add(entranceToggle);
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridwidth = 4;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	panel_settings.add(entranceToggle, gridBagConstraints1);

	exitToggle.setText(SysCallEE.EXIT.toString());	// NOI18N
	exitToggle.setMnemonic(
	    Catalog.getMnemonic("MNEM_OnExit"));	// NOI18N
	buttonGroup1.add(exitToggle);
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridwidth = 4;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	panel_settings.add(exitToggle, gridBagConstraints1);

	// a11y
	sysCallCombo.getAccessibleContext().setAccessibleDescription(
	    Catalog.get("ACSD_SystemCall") // NOI18N
	);
	entranceToggle.getAccessibleContext().setAccessibleDescription(
	    entranceToggle.getText()
	);
	exitToggle.getAccessibleContext().setAccessibleDescription(
	    exitToggle.getText()
	);

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel sysCallLabel;
    private javax.swing.JComboBox sysCallCombo;
    private javax.swing.JRadioButton entranceToggle;
    private javax.swing.JRadioButton exitToggle;
    // End of variables declaration//GEN-END:variables

    protected void assignProperties() {
	String syscall = sysCallCombo.getSelectedItem().toString();
	if (syscall.equals(si_syscalls.all()))
	    fb.setSysCall(null);
	else
	    fb.setSysCall(syscall);

	if (exitToggle.isSelected())
	    fb.setEntryExit(SysCallEE.EXIT);
	else
	    fb.setEntryExit(SysCallEE.ENTRY);
    }
    
    protected boolean propertiesAreValid() {
	Component c = sysCallCombo.getEditor().getEditorComponent();
	if (c instanceof javax.swing.text.JTextComponent) {
	    String text = ((javax.swing.text.JTextComponent)c).getText();
	    if (IpeUtils.isEmpty(text)) {
		return false;
	    } else {
		return true;
	    }
	} else if (IpeUtils.isEmpty(sysCallCombo.getSelectedItem().toString())) {
	    return false;
	}
	return true;
    }
}
