/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wao.digitalsignpdf.ui;

import com.wao.digitalsignpdf.api.APIService;
import com.wao.digitalsignpdf.api.ApiUtils;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author BaoBang
 */
public class FrameMain extends javax.swing.JFrame {

    private final int panelCount = 2;
    private int currentStep = 0;
    private final JPanel[] panels = new JPanel[panelCount];
    DialogLoading dialogLoading;
    APIService apiService;

    /**
     * Creates new form FrameMain
     */
    public FrameMain() {
        initComponents();
        // TODO add your handling code here:
        // khởi tạo loading dialog
        setIcon();
        dialogLoading = new DialogLoading(this, true, "Loading...");
        apiService = ApiUtils.getAPIService();
        // Khởi tạo Panel Danh sách hóa đơn
        PanelListOrder listOrder = new PanelListOrder(this);
        // Khởi tạo Panel danh sách KeyStore
        PanelKeyStoreList keyStrore = new PanelKeyStoreList(this);
        panels[0] = listOrder;
        panels[1] = keyStrore;

        // Add layout cho Panel chính.
        pnContent.setLayout(new BorderLayout());

        // load panel hiện tại(Panel Danh sách hóa đơn) lên panel chính
        attachPanel();
    }

    public APIService getAPIService() {
        return apiService;
    }

    /**
     * Hiện thị dialog loading
     *
     * @param message
     */
    public void showLoading(String message) {
        dialogLoading.setVisible(true);
        dialogLoading.setMessage(message);

    }

    /**
     * Ẩn dialog loading
     *
     */
    public void hideLoading() {
        if (dialogLoading.isVisible()) {
            dialogLoading.setVisible(false);
        }
    }

    /**
     * Remove tất cả component ở Panel chính Add panel phù hợp với currentStep
     * vào panel chính
     */
    public void attachPanel() {
        pnContent.removeAll();
        pnContent.add(panels[currentStep], BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    public void nextStep() {
        if (currentStep < panelCount - 1) {
            currentStep++;
        }
    }

    public void previousStep() {
        if (currentStep > 0) {
            currentStep--;
        }
    }

    public void setTextButtonNext(String text) {
        btnNext.setText(text);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnContent = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chữ kí số");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                onWindowOpened(evt);
            }
        });

        javax.swing.GroupLayout pnContentLayout = new javax.swing.GroupLayout(pnContent);
        pnContent.setLayout(pnContentLayout);
        pnContentLayout.setHorizontalGroup(
            pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );
        pnContentLayout.setVerticalGroup(
            pnContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        btnCancel.setText("Thoát");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnNext.setText("Tiếp theo");

        btnPrevious.setText("Quay lại");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnPrevious)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCancel, btnNext, btnPrevious});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(pnContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnNext)
                    .addComponent(btnPrevious))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowOpened

    }//GEN-LAST:event_onWindowOpened

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnCancelActionPerformed

    public void setPreviousButton(ActionListener listener, boolean isVisible) {
        btnPrevious.setVisible(isVisible);
        btnPrevious.addActionListener(listener);
    }

    public void setNextButton(ActionListener listener, boolean isVisible) {
        btnNext.setVisible(isVisible);
        btnNext.addActionListener(listener);
    }

    public void removeListener(ActionListener previous, ActionListener next) {
        btnNext.removeActionListener(next);
        btnPrevious.removeActionListener(previous);
    }

    public PanelListOrder getPanelListOrder() {
        return (PanelListOrder) panels[0];
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            FrameMain main = new FrameMain();
            String s = "";
            
            main.setLocationRelativeTo(null);
            main.setVisible(true);
            for(int i = 0; i < args.length; i++){
                s += args[i];
            }
            JOptionPane.showMessageDialog(main, s);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JPanel pnContent;
    // End of variables declaration//GEN-END:variables

    private void setIcon() {
        try {
            Image icon = ImageIO.read(getClass().getResource("/com/wao/digitalsignpdf/images/logo.png"));

            setIconImage(icon);
        } catch (IOException ex) {
            Logger.getLogger(FrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}