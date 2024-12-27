/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.awt.Component;
import javax.swing.JOptionPane;
import utils.RSASignature;
import utils.StringUtils;

/**
 *
 * @author Admin
 */
public class RSAController {
    private final UserDataController userDataController;

    public RSAController(UserDataController userDataController) {
        this.userDataController = userDataController;
    }
     public boolean verifyRSA(Component component) {
        try {
            byte[] data = StringUtils.generateRandomBytes(16);
            byte[] publicKey = userDataController.readPublicKey();
            byte[] signature = userDataController.signMessage(data); // TODO add your handling code here:
            byte[] modulus = new byte[128]; // Giả sử modulus là 128 byte
            byte[] exponent = new byte[3]; // Exponent thường có kích thước 3 byte
            System.arraycopy(publicKey, 0, modulus, 0, modulus.length);
            System.arraycopy(publicKey, modulus.length, exponent, 0, exponent.length);

            boolean isSuccess = RSASignature.verifySignature(modulus, exponent, data, signature);
            System.out.println("result: " + isSuccess);
            return isSuccess;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(component, "Có lỗi xảy ra.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
