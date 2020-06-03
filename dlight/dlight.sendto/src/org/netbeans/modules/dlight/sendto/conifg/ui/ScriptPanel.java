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

package org.netbeans.modules.dlight.sendto.conifg.ui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.MutableComboBoxModel;
import org.openide.awt.Mnemonics;

/**
 *
 */
public final class ScriptPanel extends JPanel {

    public ScriptPanel(String label, String toolTip) {
        initComponents();
        Mnemonics.setLocalizedText(scriptLabel, label);
        scriptFld.setToolTipText(toolTip);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scriptExecutor = new javax.swing.JComboBox();
        scrollPane = new javax.swing.JScrollPane();
        scriptFld = new javax.swing.JTextArea();
        scriptLabel = new javax.swing.JLabel();

        scriptExecutor.setEditable(true);

        scriptFld.setColumns(20);
        scriptFld.setRows(5);
        scriptFld.setToolTipText(org.openide.util.NbBundle.getMessage(ScriptPanel.class, "ScriptPanel.scriptFld.toolTipText")); // NOI18N
        scriptFld.setMargin(new java.awt.Insets(3, 3, 3, 3));
        scrollPane.setViewportView(scriptFld);

        scriptLabel.setLabelFor(scriptFld);
        org.openide.awt.Mnemonics.setLocalizedText(scriptLabel, org.openide.util.NbBundle.getMessage(ScriptPanel.class, "ScriptPanel.scriptLabel.text")); // NOI18N
        scriptLabel.setPreferredSize(new java.awt.Dimension(100, 18));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scriptLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scriptExecutor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scriptExecutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scriptLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox scriptExecutor;
    private javax.swing.JTextArea scriptFld;
    private javax.swing.JLabel scriptLabel;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables

    public void setScript(String script) {
        scriptFld.setText(script);
    }

    public String getScript() {
        return scriptFld.getText();
    }

    public void setExecutor(String executor) {
        scriptExecutor.setSelectedItem(executor);
    }

    public String getExecutor() {
        String executor = (String) scriptExecutor.getSelectedItem();

        if (scriptExecutor.getSelectedIndex() < 0) {
            // new executor..
            ((MutableComboBoxModel) scriptExecutor.getModel()).insertElementAt(executor, 0);
        }

        return executor;
    }

    public void setExecutors(String[] executors) {
        scriptExecutor.setModel(new DefaultComboBoxModel(executors));
    }
}
