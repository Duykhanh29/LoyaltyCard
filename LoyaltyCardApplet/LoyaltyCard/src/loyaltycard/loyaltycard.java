package loyaltycard;

import javacard.framework.*;

public class loyaltycard extends Applet
{
	
	private OwnerPIN pin;

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		pin = new OwnerPIN(AppletConstants.PIN_RETRIES, AppletConstants.MAX_PIN_SIZE);
		new loyaltycard().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu)
	{	
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		byte ins =  buf[ISO7816.OFFSET_INS]; 
		switch (ins)
		{
		case AppletInsConstants.INS_VERIFY_PIN:
			verifyPIN(apdu);
			break;
		case AppletInsConstants.INS_READ_USER_PIN:
			readData(apdu);
			break;
		case AppletInsConstants.INS_WRITE_USER_DATA:
			writeData(apdu);
			break;
		
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	
	 private void verifyPIN(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte pinLength = buffer[ISO7816.OFFSET_LC];

        if (pin.check(buffer, ISO7816.OFFSET_CDATA, pinLength)) {
            return;
        } else {
            ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
        }
    }
    
     private void writeData(APDU apdu) {
        if (!pin.isValidated()) {
            ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
        }

        byte[] buffer = apdu.getBuffer();
        short lc = (short) (buffer[ISO7816.OFFSET_LC] & 0xFF);
        Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, userData, (short) 0, lc);
    }

    private void readData(APDU apdu) {
    // Check if PIN is validated, throw exception if not
    if (!pin.isValidated()) {
        ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
    }

    // Get the total length of user data to send
    short totalLength = (short) userData.length;
    short bytesRead = 0;  // Track the number of bytes read

    // Set the outgoing mode to send data
    apdu.setOutgoing();

    // Loop to send data in chunks
    while (bytesRead < totalLength) {
        // Calculate the chunk size (up to 255 bytes, but no more than remaining data)
        short chunkSize = (short) Math.min(255, totalLength - bytesRead);
        
        // Set the outgoing length to the chunk size
        apdu.setOutgoingLength(chunkSize);
        
        // Send the chunk of data
        apdu.sendBytesLong(userData, bytesRead, chunkSize);

        // Update the number of bytes read
        bytesRead += chunkSize;

        // If there is more data to read, throw SW=0x6310 to indicate more data is available
        if (bytesRead < totalLength) {
            ISOException.throwIt((short) 0x6310);  // More data, continue reading
        }
    }

    // If all data has been sent, return SW=0x9000 to indicate success
    ISOException.throwIt((short) 0x9000);  // Success, all data read
}




	private void changePin(APDU apdu, byte[] buffer) {
		if (!pin.isValidated()) {
			ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
		}

		byte pin_size = buffer[ISO7816.OFFSET_LC];

		if ( pin_size > AppletConstants.MAX_PIN_SIZE) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}

		if (apdu.setIncomingAndReceive() == 0) {
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}

		pin.update(buffer, (short) (ISO7816.OFFSET_CDATA), pin_size);
		pin.reset();
	}

    
    public boolean select(){
	    if ( pin.getTriesRemaining() == 0 )
			return false;
			
		if ( pin.getTriesRemaining() == 0 )
			return false;
		
		return true;
    }
    
    public void deselect(){
	  pin.reset();
	}

}
