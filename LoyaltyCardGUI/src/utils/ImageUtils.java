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
import java.io.IOException;
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
        try (FileInputStream fis = new FileInputStream(imageFile);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
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
}
