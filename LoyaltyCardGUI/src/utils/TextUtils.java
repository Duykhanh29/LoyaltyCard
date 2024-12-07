/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author Admin
 */
public class TextUtils {
    public static String hexToText(byte[] hex) {
        return new String(hex, StandardCharsets.UTF_8);
    }
     public static byte[] textToHex(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }
}
