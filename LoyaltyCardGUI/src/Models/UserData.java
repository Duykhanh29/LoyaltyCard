/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.util.UUID;
import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class UserData {
    
    private int id;
    private String firstName;
    private String lastName;
    private boolean isMale;
    private String identification;
//    private String pin;
    private String birthday;
    private String phone;
    private byte[] image;
    private short points;
    private int gender;
    private String publicKey;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String imagePath;

    public UserData() {
    }

    public UserData(String firstName, String lastName, String phone, String identification, String birthday, boolean isMale, short points) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMale = isMale;
        this.identification = identification;
        this.birthday = birthday;
        this.phone = phone;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updateAt) {
        this.updatedAt = updateAt;
    }

        public int getGender() {
        return gender;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    

}
