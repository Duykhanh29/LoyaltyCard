/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;
import constants.AppletInsConstants;
import java.nio.charset.StandardCharsets;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.CommandAPDU;
/**
 *
 * @author Admin
 */
public class PinController {
    public boolean verifyPin(String pin) throws Exception {
        byte[] command = buildVerifyPinAPDU(pin);
        ResponseAPDU response = SmartCardConnection.getInstance()
                                                   .getChannel()
                                                   .transmit(new CommandAPDU(command));
        return isSuccess(response.getBytes());
    }

    private byte[] buildVerifyPinAPDU(String pin) {
        return new byte[]{
            (byte) 0x00, AppletInsConstants.INS_VERIFY_PIN, (byte) 0x00, (byte) 0x00,
            (byte) pin.length(),
            pin.getBytes(StandardCharsets.UTF_8)[0]
        };
    }

    private boolean isSuccess(byte[] response) {
        return response[response.length - 2] == (byte) 0x90 && response[response.length - 1] == (byte) 0x00;
    }
}
