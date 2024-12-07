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
    public byte[] buildUserData(String name, String address, String birthday, byte[] imageData, String pin) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // name 
        baos.write(name.getBytes(StandardCharsets.UTF_8));
        baos.write('|');

        // address 
        baos.write(address.getBytes(StandardCharsets.UTF_8));
        baos.write('|');

        baos.write(imageData);
        baos.write('|');

        // birthday
        baos.write(birthday.getBytes(StandardCharsets.UTF_8));
        baos.write('|');

        // pin
        baos.write(pin.getBytes(StandardCharsets.UTF_8));
        baos.write('|');

        return baos.toByteArray();
    }

    // Gửi dữ liệu người dùng lên thẻ thông minh
    public void writeUserData(String name, String address, String birthday, byte[] imageData, String pin) throws Exception {
        byte[] userData = buildUserData(name, address, birthday, imageData, pin);
        byte[] command = buildWriteAPDU(userData);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            throw new Exception("Failed to write user data.");
        }
    }

    // Đọc thông tin người dùng từ thẻ thông minh
    public UserData readUserData() throws Exception {
        List<Byte> fullData = new ArrayList<>();
        short offset = 0; // Start from the first position
        byte le = (byte) 255; // Read up to 255 bytes at a time

        while (true) {

            byte[] apdu = buildReadAPDU(offset, le);
            ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(apdu));

            // Add data to the list
            byte[] responseData = response.getData();
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

        String name = fields[0];
        String address = fields[1];

        byte[] imageData = Arrays.copyOfRange(userData, dataString.indexOf(fields[2]), dataString.lastIndexOf(fields[2]));
        String pin = fields[3];
        String birthday = fields[4];

        return new UserData(name, address, imageData, pin, birthday);
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

    private boolean isSuccess(byte[] responseBytes) {
        return responseBytes[0] == (byte) 0x90 && responseBytes[1] == (byte) 0x00;
    }
}
