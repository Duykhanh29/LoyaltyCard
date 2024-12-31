/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Admin
 */
public class AppUtils {

    public static String bytesToHex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static String byteArrayToText(byte[] byteArray) {
        StringBuilder textString = new StringBuilder();
        for (byte b : byteArray) {
            textString.append((char) b); 
        }
        return textString.toString();
    }
}
