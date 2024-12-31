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
    private String firstName;
    private String lastName;
    private boolean isMale;
    private String identification;
//    private byte[] imageData;
//    private String pin;
    private String birthday;
    private String phone;
    private byte[] image;
    private short points;

    public UserData(String firstName, String lastName,String phone,String identification, String birthday , boolean isMale,short points) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMale = isMale;
        this.identification = identification;
        this.birthday = birthday;
        this.phone = phone;
        this.points = points;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isIsMale() {
        return isMale;
    }

    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

   
    


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public short getPoints() {
        return points;
    }

    public void setPoints(short points) {
        this.points = points;
    }
    
}
