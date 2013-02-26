package ru.tehkode.mualauncher.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;
import static ru.tehkode.mualauncher.utils.Resources.*;

/**
 *
 * @author t3hk0d3
 */
public class ErrorDialog extends javax.swing.JDialog implements ClipboardOwner {

    public ErrorDialog(java.awt.Frame parent, Throwable error) {
        super(parent, true);
        
        this.setTitle(string("login_error_title"));
        
        initComponents();
        
        errorMessage.setBorder(BorderFactory.createEmptyBorder());
        errorMessage.setLineWrap(true);
        errorMessage.setEditable(false);
        errorMessage.setText(string("login_error_message"));
        
        jScrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setBorder(BorderFactory.createEmptyBorder());
        
        if(error.getCause() != null) {
            error = error.getCause();
        }
        
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        
        errorStackTrace.setText(writer.toString());
        errorStackTrace.setEditable(false);
        
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        errorMessage = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        errorStackTrace = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText(string("login_error_details"));

        closeButton.setText(string("login_error_close_button"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        copyButton.setText(string("login_error_copy_button"));
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });

        errorMessage.setEditable(false);
        errorMessage.setColumns(20);
        errorMessage.setRows(5);
        errorMessage.setAutoscrolls(false);
        errorMessage.setBorder(null);
        errorMessage.setOpaque(false);
        jScrollPane3.setViewportView(errorMessage);
        errorMessage.getAccessibleContext().setAccessibleParent(null);

        errorStackTrace.setColumns(20);
        errorStackTrace.setRows(5);
        jScrollPane4.setViewportView(errorStackTrace);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(copyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 275, Short.MAX_VALUE)
                        .addComponent(closeButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(copyButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.errorStackTrace.getText()), this);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // not a single fuck given
    }//GEN-LAST:event_copyButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton copyButton;
    private javax.swing.JTextArea errorMessage;
    private javax.swing.JTextArea errorStackTrace;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
