package loyaltycard;
public class AppletInsConstants {
	public final static byte INS_VERIFY_PIN = (byte) 0x00;
	
	public final static byte INS_WRITE_USER_DATA = (byte) 0x01;
	
	public final static byte INS_READ_USER_PIN = (byte) 0x02;

	
	public final static byte INS_CHANGE_PIN = (byte) 0x03;
	
	public final static byte INS_SET_PIN = (byte) 0x04;
	
	public final static byte INS_UPDATE_FIRST_NAME = (byte) 0x05;
	
	public final static byte INS_UPDATE_LAST_NAME = (byte) 0x06;
	
	public final static byte INS_UPDATE_BIRTHDAY = (byte) 0x07;
	
	public final static byte INS_UPDATE_PHONE = (byte) 0x08;
	
	public final static byte INS_CHECK_INIT = (byte) 0x09;
	
	public final static byte INS_GET_PUBLIC_KEY = (byte) 0x0A;
	
	public final static byte INS_SIGN_DATA = (byte) 0x0B;
	
	public final static byte INS_UPDATE_POINT = (byte) 0x10;
        
    public final static byte P1_PLUS_POINT = (byte) 0x01;
        
    public final static byte P1_SUB_POINT = (byte) 0x02;
    
    public final static byte INS_UNLOCK_PIN = (byte) 0x0C;
}
