/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author MSI 15
 */
public class RSASignature {

    // Hàm chuyển đổi mảng byte của public key thành đối tượng RSAPublicKey
    private static RSAPublicKey getRSAPublicKey(byte[] modulusBytes, byte[] exponentBytes) throws Exception {
        // Chuyển đổi mảng byte của modulus và exponent thành BigInteger
        BigInteger modulus = new BigInteger(1, modulusBytes); // BigInteger cần dấu +1 để đảm bảo không bị ký hiệu âm
        BigInteger exponent = new BigInteger(1, exponentBytes);
        
        // Tạo RSAPublicKeySpec từ modulus và exponent
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        
        // Sử dụng KeyFactory để tạo đối tượng RSAPublicKey
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    // Hàm xác minh chữ ký
    public static boolean verifySignature(byte[] modulusBytes, byte[] exponentBytes, byte[] dataToVerify, byte[] signature) {
        try {
            // Lấy đối tượng RSAPublicKey từ mảng byte của modulus và exponent
            RSAPublicKey publicKey = getRSAPublicKey(modulusBytes, exponentBytes);

            // Khởi tạo đối tượng Signature với thuật toán RSA
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);

            // Cung cấp dữ liệu cần xác minh
            sig.update(dataToVerify);

            // Xác minh chữ ký
            return sig.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
