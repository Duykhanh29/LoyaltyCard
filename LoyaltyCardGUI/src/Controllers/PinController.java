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

    private final SmartCardConnection smartCardConnection;

    public PinController(SmartCardConnection smartCardConnection) {
        this.smartCardConnection = smartCardConnection;
    }

    public boolean setPin(String pin) throws Exception {
        byte[] command = buildPinAPDU(AppletInsConstants.INS_SET_PIN, pin);
        ResponseAPDU response = smartCardConnection.getChannel()
                .transmit(new CommandAPDU(command));
        return isSuccess(response.getBytes());
    }

    public boolean verifyPin(String pin) throws Exception {
        byte[] command = buildPinAPDU(AppletInsConstants.INS_VERIFY_PIN, pin);
        ResponseAPDU response = smartCardConnection.getChannel()
                .transmit(new CommandAPDU(command));
        return isSuccess(response.getBytes());
    }

    public boolean changePIN(String pin,String newPin) throws Exception {
        String combinedPin = pin + newPin;
        byte[] command = buildPinAPDU(AppletInsConstants.INS_CHANGE_PIN, combinedPin);
        ResponseAPDU response = smartCardConnection.getChannel()
                .transmit(new CommandAPDU(command));
        return isSuccess(response.getBytes());
    }
    
    public boolean unlockPIN() throws Exception 
    {
        byte[] command = buildUnlockAPDU();
        ResponseAPDU response = smartCardConnection.getChannel()
                .transmit(new CommandAPDU(command));
        return isSuccess(response.getBytes());
    }
    
    
    private byte[] buildUnlockAPDU() {
        return new byte[]{
            (byte) 0x00, // CLA
            (byte) AppletInsConstants.INS_UNLOCK_PIN, // INS
            (byte) 0x00,
            (byte) 0x00,};
    }

    // helpers
    private byte[] buildPinAPDU(byte ins, String pin) {
        // Convert the PIN string to a byte array
        byte[] pinBytes = pin.getBytes();

        // Calculate the total length of the APDU (header + data)
        int commandLength = 5 + pinBytes.length; // 5 = CLA, INS, P1, P2, Lc

        // Create the APDU byte array
        byte[] command = new byte[commandLength];

        // Set CLA, INS, P1, P2 values
        command[0] = (byte) 0x00; // CLA
        command[1] = ins;         // INS
        command[2] = (byte) 0x00; // P1
        command[3] = (byte) 0x00; // P2

        // Set Lc (length of the data)
        command[4] = (byte) pinBytes.length;

        // Copy the PIN data into the command starting at position 5
        System.arraycopy(pinBytes, 0, command, 5, pinBytes.length);

        return command;
    }

    private boolean isSuccess(byte[] response) {
        return response[response.length - 2] == (byte) 0x90 && response[response.length - 1] == (byte) 0x00;
    }
}
