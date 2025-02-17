/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Controllers.SmartCardConnection;
import Controllers.UserDataController;
import DAO.UserDao;
import Models.UserData;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import utils.ErrorHandleUtils;
import utils.DateTimeUtils;
import utils.TextUtils;

/**
 *
 * @author Admin
 */
public class UpdateUserInfo extends javax.swing.JFrame {

    /**
     * Creates new form UpdateUserInfo
     */
    UserData userData;
    SmartCardConnection smartCardConnection;
    UserDataController userDataController;
    String initLastName;
    String initFirstName;
    String initPhone;
    String initBirthday;
    UserDao userDao;

    public UpdateUserInfo(UserData userData) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.userData = userData;
        smartCardConnection = SmartCardConnection.getInstance();
        userDataController = new UserDataController(smartCardConnection);
        userDao = UserDao.getInstance();
        initView(userData);
    }

    private UpdateUserInfo() {
    }

    private void initView(UserData userData) {
        initLastName = userData.getLastName();
        initFirstName = userData.getFirstName();
        initPhone = userData.getPhone();
        initBirthday = userData.getBirthday();

        lastNameTextField.setText(initLastName);
        firstNameTextField.setText(initFirstName);
        phoneTextField.setText(initPhone);
        setBirthday(userData);
    }

    private void setBirthday(UserData userData) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(userData.getBirthday());
            birthdayChooser.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        confirmButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        firstNameTextField = new javax.swing.JTextField();
        phoneTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lastNameTextField = new javax.swing.JTextField();
        birthdayChooser = new com.toedter.calendar.JDateChooser();
        lb_bg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Tên");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(98, 113, 80, 31));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Ngày sinh");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 80, 32));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Số điện thoại");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 90, 32));

        confirmButton.setBackground(new java.awt.Color(204, 255, 255));
        confirmButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        confirmButton.setText("Xác nhận");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });
        getContentPane().add(confirmButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(317, 342, 148, 37));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chỉnh sửa thông tin cá nhân");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 331, 37));

        cancelButton.setBackground(new java.awt.Color(204, 255, 255));
        cancelButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cancelButton.setText("Hủy");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        getContentPane().add(cancelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 342, 148, 37));
        getContentPane().add(firstNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(231, 114, 275, 31));
        getContentPane().add(phoneTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 275, 33));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Họ");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(98, 163, 80, 31));
        getContentPane().add(lastNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(231, 164, 275, 31));
        getContentPane().add(birthdayChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 275, 32));

        lb_bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/background_665x443.jpg"))); // NOI18N
        getContentPane().add(lb_bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // TODO add your handling code here:
        try {
            String phone = phoneTextField.getText();
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            Date date = birthdayChooser.getDate();

            boolean isValid = validation(firstName, lastName, phone, date);
            if (isValid) {
                boolean canUpdatePhone = canUpdatePhone(phone);
                if (canUpdatePhone) {

                    String birthday = DateTimeUtils.convertDateToString(date);
                    UserData updatedUser = new UserData(firstName, lastName, phone, userData.getIdentification(), birthday, userData.isIsMale(), userData.getPoints());
                    updatedUser.setPublicKey(userData.getPublicKey());
                    updatedUser.setId(userData.getId());
                    updatedUser.setImagePath(userData.getImagePath());  
                    boolean isUpdateSuccess = userDao.updateUser(updatedUser);
                    if (isUpdateSuccess) {
                        boolean isSucess = true;   // recognize jump to catch block
                        if (!phone.equals(initPhone)) {
                            userDataController.updatePhone(phone);
                        }
                        if (!lastName.equals(initLastName)) {
                            userDataController.updateLastName(lastName);
                        }
                        if (!firstName.equals(initFirstName)) {
                            userDataController.updateFirstName(firstName);
                        }
                        if (!birthday.equals(initBirthday)) {
                            userDataController.updateBirthday(birthday);
                        }
                        if (isSucess) {
                            JOptionPane.showMessageDialog(null, "Cập nhật thành công");
                            UserInfo userInfoView = new UserInfo();
                            this.dispose();
                            userInfoView.setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }

                }

            }

        } catch (Exception e) {
            ErrorHandleUtils.handleErrorWithException(this, e, "");
        } finally {
        }
    }//GEN-LAST:event_confirmButtonActionPerformed

    private boolean canUpdatePhone(String phone) {
        try {
            UserData checkUserData = userDao.checkExistUser(phone, userData.getIdentification());
            if (checkUserData != null) {
                if (checkUserData.getId() == userData.getId() && checkUserData.getIdentification().equals(userData.getIdentification())) {
                    // can update phone
                    return true;
                } else {

                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private boolean validation(String firstName, String lastName, String phone, Date date) {
        if (date == null) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không được để trống");
            birthdayChooser.requestFocus();
            return false;
        }
        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tên không được để trống");
            firstNameTextField.requestFocus();
            return false;
        }

        if (lastName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Họ không được để trống");
            lastNameTextField.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống");
            phoneTextField.requestFocus();
            return false;
        }

        String birthday = DateTimeUtils.convertDateToString(date);
        if (birthday == null) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không hợp lệ");
            birthdayChooser.requestFocus();
            return false;
        }

        if (!TextUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không đúng định dạng");
            phoneTextField.requestFocus();
            return false;
        }
        return true;
    }
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
        UserInfo userInfoView = new UserInfo();
        userInfoView.setVisible(true);
    }//GEN-LAST:event_cancelButtonActionPerformed

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
            java.util.logging.Logger.getLogger(UpdateUserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateUserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateUserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateUserInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateUserInfo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser birthdayChooser;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton confirmButton;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JLabel lb_bg;
    private javax.swing.JTextField phoneTextField;
    // End of variables declaration//GEN-END:variables
}
