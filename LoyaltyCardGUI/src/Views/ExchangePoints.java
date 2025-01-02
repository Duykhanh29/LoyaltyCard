/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Controllers.PinController;
import Controllers.PointController;
import Controllers.RSAController;
import Controllers.SmartCardConnection;
import Controllers.UserDataController;
import Models.UserData;
import constants.AppletConstants;
import constants.AppletInsConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import utils.NumberUtils;
import utils.RSASignature;
import utils.StringUtils;

import constants.AppletConstants;
import utils.AppUtils;

/**
 *
 * @author datlogarit
 */
public class ExchangePoints extends javax.swing.JFrame {

    /**
     * Creates new form ExchangePoints
     */
    UserData userData;
    PointController pointController;
    SmartCardConnection smartCardConnection;
    PinController pinController;
    UserDataController userDataController;

    public ExchangePoints(UserData userData) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.userData = userData;
        initViews();
        smartCardConnection = SmartCardConnection.getInstance();
        pointController = new PointController(smartCardConnection);
        pinController = new PinController(smartCardConnection);
        userDataController = new UserDataController(smartCardConnection);
    }

    private ExchangePoints() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void initViews() {
        currentPointLabel.setText(String.valueOf(userData.getPoints()));
        init();
    }
    private void init(){
        noticeText.setText("* Chú ý: Khách hàng chỉ được chọn một trong các hạn mức trên");
//        noticeText1.setText("Số điểm không được đổi sang số tiền ");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        noticeText = new javax.swing.JLabel();
        noticeText1 = new javax.swing.JLabel();
        jLable_img_1K = new javax.swing.JLabel();
        jLabelTile_1K = new javax.swing.JLabel();
        jLabelPoint_1K = new javax.swing.JLabel();
        jLabelDate_1K = new javax.swing.JLabel();
        jLabelDetail_1K = new javax.swing.JLabel();
        jLabel_gift_1K = new javax.swing.JLabel();
        lbbg_1 = new javax.swing.JLabel();
        jLable_img_2K = new javax.swing.JLabel();
        jLabelTile_2K = new javax.swing.JLabel();
        jLabelPoint_2K = new javax.swing.JLabel();
        jLabelDate_2K = new javax.swing.JLabel();
        jLabelDetail_2K = new javax.swing.JLabel();
        jLabel_gift_2K = new javax.swing.JLabel();
        lbbg_2 = new javax.swing.JLabel();
        jLable_img_3 = new javax.swing.JLabel();
        jLabelTile_3 = new javax.swing.JLabel();
        jLabelPoint_3 = new javax.swing.JLabel();
        jLabelDate_3 = new javax.swing.JLabel();
        jLabelDetail_3 = new javax.swing.JLabel();
        jLabel_gift_3 = new javax.swing.JLabel();
        lbbg_3 = new javax.swing.JLabel();
        jLable_img_4 = new javax.swing.JLabel();
        jLabelTile_4 = new javax.swing.JLabel();
        jLabelPoint_4 = new javax.swing.JLabel();
        jLabelDate_4 = new javax.swing.JLabel();
        jLabelDetail_4 = new javax.swing.JLabel();
        jLabel_gift_4 = new javax.swing.JLabel();
        lbbg_4 = new javax.swing.JLabel();
        jbbackground = new javax.swing.JLabel();

        jButton2.setBackground(new java.awt.Color(204, 204, 255));
        jButton2.setForeground(new java.awt.Color(0, 0, 51));
        jButton2.setText("Quay lại");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("ĐỔI ĐIỂM TÍCH LŨY");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Vui lòng chọn một trong các voucher bên dưới");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        jLabel3.setBackground(new java.awt.Color(255, 0, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Tổng điểm hiện có: ");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, -1, -1));

        jButton1.setBackground(new java.awt.Color(204, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Quy đổi");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 360, 160, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("2002");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, -1, -1));

        jButton3.setBackground(new java.awt.Color(204, 204, 255));
        jButton3.setForeground(new java.awt.Color(0, 0, 51));
        jButton3.setText("Quay lại");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jButton4.setBackground(new java.awt.Color(204, 204, 255));
        jButton4.setForeground(new java.awt.Color(0, 0, 51));
        jButton4.setText("Voucher");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 0, 90, -1));

        noticeText.setForeground(new java.awt.Color(255, 51, 51));
        noticeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(noticeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 430, 24));

        noticeText1.setForeground(new java.awt.Color(255, 51, 51));
        noticeText1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(noticeText1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, 450, 24));

        jLable_img_1K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png"))); // NOI18N
        getContentPane().add(jLable_img_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 60, 60));

        jLabelTile_1K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_1K.setText("VOUCHER GIẢM 20K");
        getContentPane().add(jLabelTile_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, -1, -1));

        jLabelPoint_1K.setText("Cần 10.000 điểm để quy đổi");
        getContentPane().add(jLabelPoint_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, -1, -1));

        jLabelDate_1K.setText("Date: 15/01/2025 ");
        getContentPane().add(jLabelDate_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, -1, -1));

        jLabelDetail_1K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_1K.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_1K.setText("Detail");
        jLabelDetail_1K.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        getContentPane().add(jLabelDetail_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 40, -1));

        jLabel_gift_1K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        getContentPane().add(jLabel_gift_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 50, 40));

        lbbg_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        getContentPane().add(lbbg_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 290, 60));

        jLable_img_2K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_50K_60x60.png"))); // NOI18N
        getContentPane().add(jLable_img_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 130, 60, 60));

        jLabelTile_2K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_2K.setText("VOUCHER GIẢM 50K");
        getContentPane().add(jLabelTile_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 130, -1, -1));

        jLabelPoint_2K.setText("Cần 10.000 điểm để quy đổi");
        getContentPane().add(jLabelPoint_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 150, -1, -1));

        jLabelDate_2K.setText("Date: 15/01/2025 ");
        getContentPane().add(jLabelDate_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, -1, -1));

        jLabelDetail_2K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_2K.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_2K.setText("Detail");
        getContentPane().add(jLabelDetail_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 170, 40, -1));

        jLabel_gift_2K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        getContentPane().add(jLabel_gift_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 130, 50, 40));

        lbbg_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        getContentPane().add(lbbg_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 130, 290, 60));

        jLable_img_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_100K_60x60.png"))); // NOI18N
        getContentPane().add(jLable_img_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 60, 60));

        jLabelTile_3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_3.setText("VOUCHER GIẢM 100K");
        getContentPane().add(jLabelTile_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, -1, -1));

        jLabelPoint_3.setText("Cần 10.000 điểm để quy đổi");
        getContentPane().add(jLabelPoint_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, -1, -1));

        jLabelDate_3.setText("Date: 15/01/2025 ");
        getContentPane().add(jLabelDate_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, -1, -1));

        jLabelDetail_3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_3.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_3.setText("Detail");
        getContentPane().add(jLabelDetail_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 250, 40, -1));

        jLabel_gift_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        getContentPane().add(jLabel_gift_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 50, 40));

        lbbg_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        getContentPane().add(lbbg_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 290, 60));

        jLable_img_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_200K_60x60.png"))); // NOI18N
        getContentPane().add(jLable_img_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 210, 60, 60));

        jLabelTile_4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_4.setText("VOUCHER GIẢM 200K");
        getContentPane().add(jLabelTile_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 210, -1, -1));

        jLabelPoint_4.setText("Cần 10.000 điểm để quy đổi");
        getContentPane().add(jLabelPoint_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 230, -1, -1));

        jLabelDate_4.setText("Date: 15/01/2025 ");
        getContentPane().add(jLabelDate_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, -1, -1));

        jLabelDetail_4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_4.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_4.setText("Detail");
        getContentPane().add(jLabelDetail_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 250, 40, -1));

        jLabel_gift_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        getContentPane().add(jLabel_gift_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 210, 50, 40));

        lbbg_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        getContentPane().add(lbbg_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 210, 290, 60));

        jbbackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/background_800x533.jpg"))); // NOI18N
        getContentPane().add(jbbackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            String input = textField.getText();
            short number = NumberUtils.validateAndConvertToShort(input);
            JFrame frame = new JFrame("Nhập mã PIN");
            JPasswordField passwordField = new JPasswordField(10);
            int option = JOptionPane.showConfirmDialog(frame, passwordField, "Nhập mã PIN", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                char[] pin = passwordField.getPassword();
                String pinStr = new String(pin);
                System.out.println("Mã PIN bạn nhập là: " + pinStr);
                onHandleExchangePoint(pinStr, number);
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Điểm không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
        }

    }//GEN-LAST:event_jButton1ActionPerformed
     private void onHandleExchangePoint(String pin, short number) {
        if (pin != null && !pin.isEmpty()) {
            try {
                int pinTries = pinController.verifyPin(pin);
                if ( pinTries  == AppletConstants.VERIFY_SUCCESS  ) {
                    RSAController rsaController = new RSAController(userDataController);
                    boolean isVerifyRSA = rsaController.verifyRSA(this,userData.getPublicKey());
                    if (!isVerifyRSA) {
                        JOptionPane.showMessageDialog(this, "Xác thực RSA thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    boolean isSucess = pointController.updatePoint(number, false);
                    if (isSucess) {
                        JOptionPane.showMessageDialog(this, "Đổi điểm thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        onBackToHomeView();
                    } else {
                        JOptionPane.showMessageDialog(this, "Đổi điểm không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mã PIN sai. Vui lòng thử lại. Bạn còn " + pinTries +" lần", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Mã PIN sai. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mã PIN không thể trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        onBackToHomeView();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void onBackToHomeView() {
        HomeView homeView = new HomeView();
        this.dispose();
        homeView.setVisible(true);
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        HomeView homeView = new HomeView();
        this.dispose();
        homeView.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();
        VoucherList vouchePage = new VoucherList();
        vouchePage.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void moveDetail(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveDetail
        // TODO add your handling code here:
         this.dispose();
        DetailVoucher detailVoucher = new DetailVoucher();
        detailVoucher.setVisible(true);
    }//GEN-LAST:event_moveDetail

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
            java.util.logging.Logger.getLogger(ExchangePoints.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExchangePoints.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExchangePoints.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExchangePoints.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExchangePoints().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currentPointLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelDate_1K;
    private javax.swing.JLabel jLabelDate_2K;
    private javax.swing.JLabel jLabelDate_3;
    private javax.swing.JLabel jLabelDate_4;
    private javax.swing.JLabel jLabelDetail_1K;
    private javax.swing.JLabel jLabelDetail_2K;
    private javax.swing.JLabel jLabelDetail_3;
    private javax.swing.JLabel jLabelDetail_4;
    private javax.swing.JLabel jLabelPoint_1K;
    private javax.swing.JLabel jLabelPoint_2K;
    private javax.swing.JLabel jLabelPoint_3;
    private javax.swing.JLabel jLabelPoint_4;
    private javax.swing.JLabel jLabelTile_1K;
    private javax.swing.JLabel jLabelTile_2K;
    private javax.swing.JLabel jLabelTile_3;
    private javax.swing.JLabel jLabelTile_4;
    private javax.swing.JLabel jLabel_gift_1K;
    private javax.swing.JLabel jLabel_gift_2K;
    private javax.swing.JLabel jLabel_gift_3;
    private javax.swing.JLabel jLabel_gift_4;
    private javax.swing.JLabel jLable_img_1K;
    private javax.swing.JLabel jLable_img_2K;
    private javax.swing.JLabel jLable_img_3;
    private javax.swing.JLabel jLable_img_4;
    private javax.swing.JLabel jbbackground;
    private javax.swing.JLabel lbbg_1;
    private javax.swing.JLabel lbbg_2;
    private javax.swing.JLabel lbbg_3;
    private javax.swing.JLabel lbbg_4;
    private javax.swing.JLabel noticeText;
    private javax.swing.JLabel noticeText1;
    // End of variables declaration//GEN-END:variables
}
