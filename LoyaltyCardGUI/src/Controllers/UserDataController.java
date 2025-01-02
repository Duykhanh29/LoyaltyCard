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
import constants.AppletConstants;
import constants.AppletInsConstants;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import utils.StringUtils;

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
    public byte[] buildUserData(int userId, String firstName, String lastName, String phone, String cccd, String birthday, boolean isMale, String pin) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

//        if (imageData != null && imageData.length > 0) {
//            baos.write(imageData);
//        }
//        baos.write('|');
        // user id
        short convertedUserId = (short) userId; // Convert int to short
        baos.write(ByteBuffer.allocate(2).putShort(convertedUserId).array());
        baos.write('|');

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

    public boolean checkExistedData() throws Exception {
        byte[] command = buildCheckExistedAPDU();
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (isNoData(response.getBytes())) {
            return false;
        }
        return isSuccess(response.getBytes());
    }

    public boolean writeUserData(int userId, String firstName, String lastName, String phone, String cccd, String birthday, boolean isMale, String pin) throws Exception {
        byte[] userData = buildUserData(userId, firstName, lastName, phone, cccd, birthday, isMale, pin);
        byte[] command = buildWriteAPDU(userData);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            if (isActionFail(response.getBytes())) {
                return false;
            }
            throw new Exception("Failed to write user data.");
        }
        return isSuccess(response.getBytes());
    }

    public UserData readUserData() throws Exception {
        List<Byte> fullData = new ArrayList<>();
        short offset = 0;
        byte le = (byte) 0xFF; // Read up to 255 bytes at a time

        while (true) {
            byte[] apdu = buildReadAPDU(offset, le);
            ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(apdu));

            // Add data to the list
            byte[] responseData = response.getData();
            for (byte b : responseData) {
                fullData.add(b);
            }

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

    public byte[] readPublicKey() throws Exception {
        List<Byte> fullData = new ArrayList<>();
        short offset = 0;
        byte le = (byte) 0xFF; // Read up to 255 bytes at a time
        while (true) {
            byte[] apdu = buildReadKeyAPDU(offset, le);
            ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(apdu));

            // Add data to the list
            byte[] responseData = response.getData();
            for (byte b : responseData) {
                fullData.add(b);
            }

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

        return finalData;
    }

    public byte[] signMessage(byte[] data) throws Exception {
        byte le = (byte) 0xFF; // Read up to 255 bytes at a time

        byte[] apdu = buildSignDataAPDU(data, le);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(apdu));

        int sw = response.getSW();
        byte[] responseData;
        if (sw == 0x9000) { // SW_NO_ERROR: Hoàn thành
            responseData = response.getData();
        } else {
            throw new CardException("Error during reading data: SW=" + Integer.toHexString(sw));
        }
        return responseData;
    }

    public boolean updateFirstName(String firstName) throws Exception {
        byte[] firstNameByte = parseStringToByte(firstName);
        byte[] command = buildUpdateFisrtNameAPDU(firstNameByte);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            if (isActionFail(response.getBytes())) {
                return false;
            }
            throw new Exception("Failed to write user data.");
        }
        return isSuccess(response.getBytes());
    }

    public boolean updateLastName(String lastName) throws Exception {
        byte[] lastNameByte = parseStringToByte(lastName);
        byte[] command = buildUpdateLastNameAPDU(lastNameByte);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            if (isActionFail(response.getBytes())) {
                return false;
            }
            throw new Exception("Failed to write user data.");
        }
        return isSuccess(response.getBytes());
    }

    public boolean updateBirthday(String birthday) throws Exception {
        byte[] birthdayByte = parseStringToByte(birthday);
        byte[] command = buildUpdateBirthdayAPDU(birthdayByte);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            if (isActionFail(response.getBytes())) {
                return false;
            }
            throw new Exception("Failed to write user data.");
        }
        return isSuccess(response.getBytes());
    }

    public boolean updatePhone(String phone) throws Exception {
        byte[] phoneByte = parseStringToByte(phone);
        byte[] command = buildUpdatePhoneAPDU(phoneByte);
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            if (isActionFail(response.getBytes())) {
                return false;
            }
            throw new Exception("Failed to write user data.");
        }
        return isSuccess(response.getBytes());
    }

    // Parse user data from byte[] (including name, age, photo, PIN)
    public UserData parseUserData(byte[] userData) throws Exception {

        byte[] userIdBytes = Arrays.copyOfRange(userData, 0, 2);
        byte[] userWihoutId = Arrays.copyOfRange(userData, 2, userData.length);

        byte[] pointBytes = Arrays.copyOfRange(userWihoutId, userWihoutId.length - 2, userWihoutId.length);
        byte[] userWihoutPoint = Arrays.copyOfRange(userWihoutId, 0, userWihoutId.length - 2);
        // Convert byte[] to string and split by separator '|'
        String dataString = new String(userWihoutPoint, StandardCharsets.UTF_8);
        String[] fields = dataString.split("\\|");

        String firstName = fields[0];
        String lastName = fields[1];
        String phone = fields[2];
        String identification = fields[3];
        String birthday = fields[4];

        boolean isMale = true;
        String genderData = fields[5];
        if (genderData.contains("\u0001")) {
            isMale = true;
        } else if (genderData.contains("\u0000")) {
            isMale = false;
        }

        short point = 0;
        if (pointBytes.length == 2) {
            point = (short) ((pointBytes[0] << 8) | (pointBytes[1] & 0xFF));
        }

//        byte[] imageData = Arrays.copyOfRange(userData, dataString.indexOf(fields[2]), dataString.lastIndexOf(fields[2]));
//        String pin = fields[3];
        short id = 0;
        if (pointBytes.length == 2) {
            id = (short) ((userIdBytes[0] << 8) | (userIdBytes[1] & 0xFF));
        }
        int userId = (int) id;
        UserData user = new UserData(firstName, lastName, phone, identification, birthday, isMale, point);
        user.setId(userId);
        return user;
    }

    // Function to find the position of the first delimiter '|' in the byte array
    private int findFirstSeparatorIndex(byte[] userData) {
        for (int i = 0; i < userData.length; i++) {
            if (userData[i] == '|') {
                return i;
            }
        }
        return -1; // Nếu không tìm thấy, trả về -1
    }

    private byte[] buildCheckExistedAPDU() {
        return new byte[]{
            (byte) 0x00, // CLA
            (byte) AppletInsConstants.INS_CHECK_INIT, // INS
            (byte) 0x00,
            (byte) 0x00,};
    }

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

    private byte[] buildReadAPDU(short offset, byte le) {
        return new byte[]{
            (byte) 0x00, // CLA
            (byte) AppletInsConstants.INS_READ_USER_DATA, // INS
            (byte) (offset >> 8), // P1 (byte cao của offset)
            (byte) (offset & 0xFF), // P2 (byte thấp của offset)
            le // Le
        };
    }

    private byte[] buildReadKeyAPDU(short offset, byte le) {
        return new byte[]{
            (byte) 0x00, // CLA
            (byte) AppletInsConstants.INS_GET_PUBLIC_KEY, // INS
            (byte) (offset >> 8), // P1 (byte cao của offset)
            (byte) (offset & 0xFF), // P2 (byte thấp của offset)
            le // Le
        };
    }

    private byte[] buildSignDataAPDU(byte[] data, byte le) {
        byte[] apduCommand = new byte[6 + data.length]; // 6 = 5 (CLA, INS, P1, P2, Lc) + 1 (LE)
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = AppletInsConstants.INS_SIGN_DATA; // INS
        apduCommand[2] = (byte) 0x00; // P1
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) data.length; // Lc (Độ dài dữ liệu)
        System.arraycopy(data, 0, apduCommand, 5, data.length); // Copy dữ liệu vào apduCommand
        apduCommand[5 + data.length] = le;
        return apduCommand;
    }

    private byte[] buildUpdateFisrtNameAPDU(byte[] firstName) {
        byte[] apduCommand = new byte[5 + firstName.length];
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = AppletInsConstants.INS_UPDATE_FIRST_NAME; // INS
        apduCommand[2] = (byte) 0x00; // P1
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) firstName.length; // Lc
        System.arraycopy(firstName, 0, apduCommand, 5, firstName.length);
        return apduCommand;
    }

    private byte[] buildUpdateLastNameAPDU(byte[] lastName) {
        byte[] apduCommand = new byte[5 + lastName.length];
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = AppletInsConstants.INS_UPDATE_LAST_NAME; // INS
        apduCommand[2] = (byte) 0x00; // P1
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) lastName.length; // Lc
        System.arraycopy(lastName, 0, apduCommand, 5, lastName.length);
        return apduCommand;
    }

    private byte[] buildUpdateBirthdayAPDU(byte[] birthday) {
        byte[] apduCommand = new byte[5 + birthday.length];
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = AppletInsConstants.INS_UPDATE_BIRTHDAY; // INS
        apduCommand[2] = (byte) 0x00; // P1
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) birthday.length; // Lc
        System.arraycopy(birthday, 0, apduCommand, 5, birthday.length);
        return apduCommand;
    }

    private byte[] buildUpdatePhoneAPDU(byte[] phone) {
        byte[] apduCommand = new byte[5 + phone.length];
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = AppletInsConstants.INS_UPDATE_PHONE; // INS
        apduCommand[2] = (byte) 0x00; // P1
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) phone.length; // Lc
        System.arraycopy(phone, 0, apduCommand, 5, phone.length);
        return apduCommand;
    }

    public byte[] parseStringToByte(String value) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(value.getBytes(StandardCharsets.UTF_8));
        return baos.toByteArray();
    }

    public boolean resetCardData() throws CardException, Exception {
        byte[] command = buildResetCardDataAPDU();
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));
        if (!isSuccess(response.getBytes())) {
            if (isActionFail(response.getBytes())) {
                return false;
            }
            throw new Exception("Failed to write user data.");
        }
        return isSuccess(response.getBytes());
    }

    private byte[] buildResetCardDataAPDU() {
        return new byte[]{
            (byte) 0x00, // CLA
            (byte) AppletInsConstants.INS_RESET_CARD_DATA, // INS
            (byte) 0x00,
            (byte) 0x00,};
    }

//    public boolean setImage(byte[] imageData){
//        
//    }
    private boolean isSuccess(byte[] responseBytes) {
        return responseBytes[0] == (byte) 0x90 && responseBytes[1] == (byte) 0x00;
    }

    private boolean isNoData(byte[] responseBytes) {
        return responseBytes[0] == (byte) 0x6A && responseBytes[1] == (byte) 0x88;
    }

    private boolean isActionFail(byte[] responseBytes) {
        return responseBytes[0] == (byte) 0x9F && responseBytes[1] == (byte) 0xFF;
    }
}
