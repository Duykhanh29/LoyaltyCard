/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.awt.HeadlessException;
import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;
import utils.AppUtils;

/**
 *
 * @author Admin
 */
public class SmartCardConnection {
    public static final byte[] AID_APPLET = {(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, (byte)0x66,(byte)0x00};
    private Card card;
    private TerminalFactory factory;
    private CardTerminal terminal;
    private CardChannel channel;
    private List<CardTerminal> terminals;
    private ResponseAPDU response;
    
    public SmartCardConnection(){}
    
    private static SmartCardConnection instance;
    
    public static synchronized SmartCardConnection getInstance() {
        if (instance == null) {
            instance = new SmartCardConnection();
        }
        return instance;
    }

    public Card getCard() {
        return card;
    }

    public TerminalFactory getFactory() {
        return factory;
    }

    public CardTerminal getTerminal() {
        return terminal;
    }

    public CardChannel getChannel() {
        return channel;
    }

    public ResponseAPDU getResponse() {
        return response;
    }
    
    
    public boolean connectCard(){
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("*");
            channel = card.getBasicChannel();
            if(channel==null){
                return false;
            }
            response = channel.transmit(new CommandAPDU(0x00, (byte)0xA4, 0x04, 0x00, AID_APPLET));
            String check = Integer.toHexString(response.getSW());
            System.out.println("Response: " +AppUtils.bytesToHex(response.getData()));
            switch (check) {
                case "9000" -> {
                    return true;
                }
                case "6400" -> {
                    JOptionPane.showMessageDialog(null, "Thẻ đã bị vô hiệu hóa");
                    return true;
                }
                default -> {
                    return false;
                }
            }
        } catch (HeadlessException | CardException e) {
            System.out.println("Somwthing went wrong " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean disconnect(){
        try {
            if(card!=null){
                card.disconnect(false);
                return true;
            }
        } catch (CardException e) {
            System.out.println("Somwthing went wrong " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
}
