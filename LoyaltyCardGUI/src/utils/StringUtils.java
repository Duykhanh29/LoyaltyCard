/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.security.SecureRandom;

/**
 *
 * @author MSI 15
 */
public class StringUtils {
    
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();

    public static byte[] generateRandomBytes(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);  // Tạo chuỗi ngẫu nhiên với byte
        return randomBytes;
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));  // Tạo chuỗi ngẫu nhiên ký tự
        }
        return sb.toString();
    }
    
}
