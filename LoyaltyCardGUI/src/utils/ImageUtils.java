/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;

/**
 *
 * @author Admin
 */
public class ImageUtils {

    public static byte[] imageToByteArray(String imagePath) throws Exception {
        BufferedImage image = ImageIO.read(new File(imagePath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos); // Có thể thay "png" bằng định dạng khác
        baos.flush();
        return baos.toByteArray();
    }

    public byte[] convertImageToByteArray(File imageFile) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(imageFile); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    public static BufferedImage byteArrayToImage(byte[] byteArray) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray)) {
            return ImageIO.read(bais);
        }
    }

    private static final String UPLOAD_DIR = "uploads"; // Thư mục lưu ảnh

    public static String saveImage(byte[] imageData, String fileName) throws IOException {
        // Kiểm tra thư mục "uploads" có tồn tại không, nếu không thì tạo mới
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Tạo đường dẫn lưu trữ ảnh
        String newFileName = System.currentTimeMillis() + "_" + fileName; // Đặt tên file mới
        File destinationFile = new File(uploadDir, newFileName);

        // Ghi dữ liệu byte[] vào file
        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            fos.write(imageData);
        }

        // Trả về đường dẫn file (để lưu vào DB)
        return destinationFile.getPath();
    }

    public static byte[] loadImage(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File không tồn tại: " + filePath);
        }

        byte[] fileData = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileData);
        }
        return fileData;
    }
}
