package ru.tehkode.mualauncher.gui;

import java.util.Locale;
import javax.swing.JOptionPane;
import ru.tehkode.mualauncher.LauncherOptions;
import ru.tehkode.mualauncher.utils.Logger;

import static ru.tehkode.mualauncher.utils.Resources.*;
import ru.tehkode.mualauncher.widgets.LanguageComboBox;

/**
 *
 * @author t3hk0d3
 */
public class OptionsDialog extends javax.swing.JDialog {

    private final LauncherOptions options;

    public OptionsDialog(java.awt.Frame parent, LauncherOptions options) {
        super(parent, string("options_window_title"), true);

        this.options = options;

        initComponents();

        this.setLocationRelativeTo(parent);

        this.jvmOptionsText.setText(this.options.getJvmOptions());
        this.forceReloadToggle.setSelected(this.options.isForceReload());
        ((LanguageComboBox)this.lanugageCombo).setSelectedLocale(this.options.getLocale());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancelButton = new javax.swing.JButton();
        doneButton = new javax.swing.JButton();
        forceReloadToggle = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jvmOptionsText = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        lanugageCombo = new LanguageComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cancelButton.setText(string("options_cancel_button"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        doneButton.setText(string("options_done_button"));
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        forceReloadToggle.setText(string("force_reload_button"));

        jLabel1.setText(string("jvm_options_label"));

        jScrollPane1.setViewportView(jvmOptionsText);
        jvmOptionsText.getAccessibleContext().setAccessibleName("");

        jLabel2.setText(string("language_label"));

        lanugageCombo.setModel(null);
        lanugageCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lanugageComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(forceReloadToggle, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(doneButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lanugageCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lanugageCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(forceReloadToggle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(doneButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        this.options.setJvmOptions(this.jvmOptionsText.getText());
        this.options.setForceReload(this.forceReloadToggle.isSelected());

        Locale selectedLocale = ((LanguageComboBox) this.lanugageCombo).getSelectedLocale();

        if (selectedLocale != null) {              
            if (!selectedLocale.equals(this.options.getLocale())) {
                JOptionPane.showMessageDialog(this, string("translation_reload_message"), string("translation_reload_title"), JOptionPane.INFORMATION_MESSAGE);
            }
            
            this.options.setLocale(selectedLocale);
        }
        // set other options here

        this.options.save();
        this.setVisible(false);
    }//GEN-LAST:event_doneButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void lanugageComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lanugageComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lanugageComboActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton doneButton;
    private javax.swing.JToggleButton forceReloadToggle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jvmOptionsText;
    private javax.swing.JComboBox lanugageCombo;
    // End of variables declaration//GEN-END:variables
}
