/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import constants.AppletInsConstants;
import javax.smartcardio.*;

/**
 *
 * @author Admin
 */
public class PointController {

    private final SmartCardConnection smartCardConnection;

    public PointController(SmartCardConnection smartCardConnection) {
        this.smartCardConnection = smartCardConnection;
    }

    private byte[] buildUpdatePointAPDU(short points, byte p1) {
        // Chuyển đổi short thành 2 byte
        byte[] pointBytes = new byte[2];
        pointBytes[0] = (byte) (points >> 8);  // Byte cao
        pointBytes[1] = (byte) (points & 0xFF);  // Byte thấp

        // Tạo APDU command cho việc thêm/giảm điểm
        byte[] apduCommand = new byte[5 + pointBytes.length];
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = AppletInsConstants.INS_UPDATE_POINT; // INS
        apduCommand[2] = p1; // P1 (0x01 cho thêm điểm, 0x02 cho giảm điểm)
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) pointBytes.length; // Lc (Số byte dữ liệu đi kèm)

        // Copy dữ liệu số điểm vào APDU command
        System.arraycopy(pointBytes, 0, apduCommand, 5, pointBytes.length);

        return apduCommand;
    }

    public boolean updatePoint(short points, boolean isAdd) throws Exception {
        // Xác định P1: 0x01 cho thêm điểm, 0x02 cho giảm điểm
        byte p1 = isAdd ? AppletInsConstants.P1_PLUS_POINT : AppletInsConstants.P1_SUB_POINT;

        // Tạo APDU command để gửi yêu cầu thêm/giảm điểm
        byte[] command = buildUpdatePointAPDU(points, p1);

        // Gửi APDU và nhận phản hồi
        ResponseAPDU response = smartCardConnection.getChannel().transmit(new CommandAPDU(command));

        // Kiểm tra kết quả
        if (!isSuccess(response.getBytes())) {
            if (isActionFail(response.getBytes())) {
                return false;
            }
            throw new Exception("Failed to update points.");
        }

        return isSuccess(response.getBytes());
    }

    private boolean isSuccess(byte[] responseBytes) {
        return responseBytes[0] == (byte) 0x90 && responseBytes[1] == (byte) 0x00;
    }

    private boolean isActionFail(byte[] responseBytes) {
        return responseBytes[0] == (byte) 0x9F && responseBytes[1] == (byte) 0xFF;
    }

}
