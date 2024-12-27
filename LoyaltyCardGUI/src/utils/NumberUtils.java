/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Admin
 */
public class NumberUtils {
      public static short validateAndConvertToShort(String input) throws NumberFormatException, IllegalArgumentException {
        // Kiểm tra nếu chuỗi trống
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("Dữ liệu không thể trống");
        }

        // Kiểm tra nếu chuỗi là số
        short value = Short.parseShort(input);  // Nếu không phải số, sẽ ném lỗi NumberFormatException

        return value;
    }
}
