/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Controllers.SmartCardConnection;
import Controllers.UserDataController;
import Models.UserData;
import utils.ErrorHandleUtils;

/**
 *
 * @author Admin
 */
public class HomeView extends javax.swing.JFrame {

    /**
     * Creates new form HomeView
     */
    
    UserDataController userDataController;
    SmartCardConnection smartCardConnection;
    public HomeView() {
        initComponents();
        this.setLocationRelativeTo(null);
        smartCardConnection = SmartCardConnection.getInstance();
        userDataController = new UserDataController(smartCardConnection);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        loyaltyPointsTextView = new javax.swing.JLabel();
        userInfoButton = new javax.swing.JButton();
        changePINButton = new javax.swing.JButton();
        redeemPointButton = new javax.swing.JButton();
        transactionHistoryButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        chargePointButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(204, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Màn hình chính");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Điểm tích lũy hiện tại");

        loyaltyPointsTextView.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        loyaltyPointsTextView.setForeground(new java.awt.Color(255, 0, 0));
        loyaltyPointsTextView.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loyaltyPointsTextView.setText("400");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(loyaltyPointsTextView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loyaltyPointsTextView, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        userInfoButton.setBackground(new java.awt.Color(153, 153, 255));
        userInfoButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        userInfoButton.setForeground(new java.awt.Color(0, 51, 51));
        userInfoButton.setText("Thông tin cá nhân");
        userInfoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userInfoButtonActionPerformed(evt);
            }
        });

        changePINButton.setBackground(new java.awt.Color(153, 153, 255));
        changePINButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        changePINButton.setForeground(new java.awt.Color(0, 0, 51));
        changePINButton.setText("Đổi PIN");
        changePINButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePINButtonActionPerformed(evt);
            }
        });

        redeemPointButton.setBackground(new java.awt.Color(153, 153, 255));
        redeemPointButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        redeemPointButton.setForeground(new java.awt.Color(0, 51, 51));
        redeemPointButton.setText("Đổi điểm");
        redeemPointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redeemPointButtonActionPerformed(evt);
            }
        });

        transactionHistoryButton.setBackground(new java.awt.Color(153, 153, 255));
        transactionHistoryButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        transactionHistoryButton.setForeground(new java.awt.Color(0, 51, 51));
        transactionHistoryButton.setText("Lịch sử giao dịch");
        transactionHistoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionHistoryButtonActionPerformed(evt);
            }
        });

        disconnectButton.setBackground(new java.awt.Color(255, 204, 204));
        disconnectButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        disconnectButton.setForeground(new java.awt.Color(153, 0, 0));
        disconnectButton.setText("Ngắt kết nối");
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });

        chargePointButton.setBackground(new java.awt.Color(153, 153, 255));
        chargePointButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        chargePointButton.setForeground(new java.awt.Color(0, 51, 51));
        chargePointButton.setText("Tích điểm");
        chargePointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargePointButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(redeemPointButton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(disconnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chargePointButton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(userInfoButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changePINButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transactionHistoryButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userInfoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(redeemPointButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(transactionHistoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chargePointButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changePINButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(disconnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void changePINButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePINButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        ChangePINCode changePinView = new ChangePINCode();
        changePinView.setVisible(true);
        
    }//GEN-LAST:event_changePINButtonActionPerformed

    private void transactionHistoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionHistoryButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transactionHistoryButtonActionPerformed

    private void userInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userInfoButtonActionPerformed
        // TODO add your handling code here:
        try {
//           UserData userData = userDataController.readUserData();
            this.dispose();
            UserInfo userInfoView = new UserInfo();
            userInfoView.setVisible(true);
           
        } catch (Exception e) {
            ErrorHandleUtils.handleErrorWithException(this, e, "Lỗi ko đọc được thông tin 1");
        } finally {
        }
    }//GEN-LAST:event_userInfoButtonActionPerformed

    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectButtonActionPerformed
        // TODO add your handling code here:
        smartCardConnection.disconnect();
        this.dispose();
        ConnectionForm connectionForm = new ConnectionForm();
        connectionForm.setVisible(true);
    }//GEN-LAST:event_disconnectButtonActionPerformed

    private void chargePointButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargePointButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        AccummulatePoints accummulatePoints = new AccummulatePoints();
        accummulatePoints.setVisible(true);
    }//GEN-LAST:event_chargePointButtonActionPerformed

    private void redeemPointButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redeemPointButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        ExchangePoints exchangePoints = new ExchangePoints();
        exchangePoints.setVisible(true);
    }//GEN-LAST:event_redeemPointButtonActionPerformed

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
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changePINButton;
    private javax.swing.JButton chargePointButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel loyaltyPointsTextView;
    private javax.swing.JButton redeemPointButton;
    private javax.swing.JButton transactionHistoryButton;
    private javax.swing.JButton userInfoButton;
    // End of variables declaration//GEN-END:variables
}
