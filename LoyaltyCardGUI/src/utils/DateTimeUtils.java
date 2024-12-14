package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Admin
 */
public class DateTimeUtils {

    public static String convertDateToString(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String result = format.format(date);
            return result;
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    public static boolean isValidDate(Date date) {
        try {
            // Chuyển đổi Date -> String -> Date để kiểm tra tính nhất quán
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            format.setLenient(false); // Không cho phép ngày không hợp lệ
            String formattedDate = DateTimeUtils.convertDateToString(date);
            Date parsedDate = format.parse(formattedDate); // Parse lại
            return date.equals(parsedDate); // So sánh ngày gốc và ngày parse
        } catch (Exception e) {
            return false; // Nếu có lỗi, ngày không hợp lệ
        }
    }
}
