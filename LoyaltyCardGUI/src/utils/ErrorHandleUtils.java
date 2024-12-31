package utils;


import java.awt.Component;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class ErrorHandleUtils {
    public static void handleErrorWithException(Component component,Exception e,String title ){
        JOptionPane.showMessageDialog(component, title);
        System.out.println("Somwthing went wrong " + e.toString());
        e.printStackTrace();
    }

}
