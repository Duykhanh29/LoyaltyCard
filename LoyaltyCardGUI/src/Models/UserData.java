/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.UUID;

/**
 *
 * @author Admin
 */
public class UserData {
    private String name;
    private String address;
//    private int id;
    private String cardNumber;
    private byte[] imageData;
    private String pin;
    private String birthday;

    public UserData(String name, String address, byte[] imageData,String pin,String birthday) {
        this.name = name;
        this.address = address;
//        this.id = id;
        this.cardNumber = generateUniqueCardNumber();
        this.imageData = imageData;
        this.pin = pin;
        this.birthday = birthday;
    }
    
    // just temporary  
    private String generateUniqueCardNumber() {
        return UUID.randomUUID().toString();  // Tạo UUID mới
    }
    public UserData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    
    
    
}
