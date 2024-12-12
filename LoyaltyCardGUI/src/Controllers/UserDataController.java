/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import javax.smartcardio.*;
import java.util.Arrays;
import Models.UserData;
import constants.AppletInsConstants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class UserDataController {

    private final SmartCardConnection smartCardConnection;

    public UserDataController(SmartCardConnection smartCardConnection) {
        this.smartCardConnection = smartCardConnection;
    }

    // Ghép thông tin người dùng thành một mảng byte để lưu vào thẻ
    public byte[] buildUserData(String firstName, String lastName, String phone, String cccd, String birthday,boolean isMale, String pin) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // firstName 
        baos.write(firstName.getBytes(StandardCharsets.UTF_8));
        baos.write('|');

        // lastName 
        baos.write(lastName.getBytes(StandardCharsets.UTF_8));
        baos.write('|');
        
         // phone 
        baos.write(phone.getBytes(StandardCharsets.UTF_8));
        baos.write('|');
        
//        baos.write(imageData);
//        baos.write('|');
        
        // cccd
        baos.write(cccd.getBytes(StandardCharsets.UTF_8));
        baos.write('|');

        // birthday
        baos.write(birthday.getBytes(StandardCharsets.UTF_8));
        baos.write('|');
        
        // birthday
        baos.write(isMale ? 1 : 0);
        baos.write('|');

        // pin
        baos.write(pin.getBytes(StandardCharsets.UTF_8));
//        baos.write('|');

        return baos.toByteArray();
    }

    // Gửi dữ liệu người dùng lên thẻ thông minh
    public boolean writeUserData(String firstName, String lastName, String phone, String cccd, String birthday,boolean isMale, String pin) throws Exception {
        byte[] userData = buildUserData(firstName, lastName, phone, cccd, birthday,isMale,pin);
        byte[] command = buildWriteAPDU(userData);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            throw new Exception("Failed to write user data.");
        }
        return isSuccess(response.getBytes());
    }

    // Đọc thông tin người dùng từ thẻ thông minh
    public UserData readUserData() throws Exception {
        List<Byte> fullData = new ArrayList<>();
        short offset = 0; // Start from the first position
        byte le = (byte) 0xFF; // Read up to 255 bytes at a time
   
        while (true) {
            byte[] apdu = buildReadAPDU(offset, le);
            ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(apdu));

            // Add data to the list
            byte[] responseData = response.getData();
            byte[] dataWithoutSW = Arrays.copyOfRange(responseData, 0, responseData.length - 2);
            for (byte b : responseData) {
                fullData.add(b);
            }

            // Check return status (SW)
            int sw = response.getSW();
            if (sw == 0x9000) { // SW_NO_ERROR: Hoàn thành
                break;
            } else if (sw != 0x6310) { // SW_WARNING_STATE_NC: More data, continue
                throw new CardException("Error during reading data: SW=" + Integer.toHexString(sw));
            }

            // Increment offset to read next data
            offset += responseData.length;
        }

        // Convert data list to byte array
        byte[] finalData = new byte[fullData.size()];
        for (int i = 0; i < fullData.size(); i++) {
            finalData[i] = fullData.get(i);
        }

        return parseUserData(finalData);
    }

    // Parse user data from byte[] (including name, age, photo, PIN)
    public UserData parseUserData(byte[] userData) throws Exception {
        // Convert byte[] to string and split by separator '|'
        String dataString = new String(userData, StandardCharsets.UTF_8);
        String[] fields = dataString.split("\\|");

        String firstName = fields[0];
        String lastName = fields[1];
        String phone = fields[2];
        String identification = fields[3];
        String birthday = fields[4];

        boolean isMale=true;
        String genderData = fields[fields.length-1];
        if(genderData.contains("\u0001")){
            isMale = true;
        }else if(genderData.contains("\u0000")){
            isMale =false;
        }
        
//        byte[] imageData = Arrays.copyOfRange(userData, dataString.indexOf(fields[2]), dataString.lastIndexOf(fields[2]));
//        String pin = fields[3];
       
           
        return new UserData(firstName, lastName, phone, identification, birthday,isMale);
    }

    // Xây dựng APDU để ghi dữ liệu lên thẻ
    private byte[] buildWriteAPDU(byte[] userData) {
        byte[] apduCommand = new byte[5 + userData.length];
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = AppletInsConstants.INS_WRITE_USER_DATA; // INS
        apduCommand[2] = (byte) 0x00; // P1
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) userData.length; // Lc
        System.arraycopy(userData, 0, apduCommand, 5, userData.length);
        return apduCommand;
    }

    // Xây dựng APDU để đọc dữ liệu từ thẻ
    private byte[] buildReadAPDU(short offset, byte le) {
        return new byte[]{
            (byte) 0x00, // CLA
            (byte) AppletInsConstants.INS_READ_USER_DATA, // INS
            (byte) (offset >> 8), // P1 (byte cao của offset)
            (byte) (offset & 0xFF), // P2 (byte thấp của offset)
            le // Le
        };
    }
    
    private short getTotalUserDataLength() throws CardException {
    byte[] apdu = buildGetLengthAPDU();
    ResponseAPDU response =  smartCardConnection.getChannel().transmit(new CommandAPDU(apdu));
    byte[] lengthData = response.getData();
    return (short) ((lengthData[0] & 0xFF) << 8 | (lengthData[1] & 0xFF)); // Chuyển từ 2 byte sang short
}
    
    private byte[] buildGetLengthAPDU(){
         return new byte[]{
            (byte) 0x00, // CLA
            (byte) AppletInsConstants.INS_GET_USER_DATA_LENGTH, // INS
            (byte) 0x00,
            (byte) 0x00, 
        };
    }
    
    
    
//    public boolean setImage(byte[] imageData){
//        
//    }

    private boolean isSuccess(byte[] responseBytes) {
        return responseBytes[0] == (byte) 0x90 && responseBytes[1] == (byte) 0x00;
    }
}
