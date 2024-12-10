package loyaltycard;

import javacard.framework.*;


public class loyaltycard extends Applet
{
	
	private static OwnerPIN pin;
	private static byte[] userData = new byte[1024];

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		pin = new OwnerPIN(AppletConstants.PIN_RETRIES, AppletConstants.MAX_PIN_SIZE);
		// byte[] pinArr = {1,2,3,4,5,6};
		// pin.update(pinArr, (short) 0, (byte)pinArr.length);
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
		case AppletInsConstants.INS_CHANGE_PIN:
			changePin(apdu);
			break;
		case AppletInsConstants.INS_SET_PIN:
			createPIN(apdu);
			break;
		
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
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
    // Check if the PIN is validated, throw an exception if not
    if (!pin.isValidated()) {
        ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
    }

    // Check if the length of userData exceeds the maximum size for short
    if (userData.length > Short.MAX_VALUE) {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID);
    }

    // Get the total length of user data to send
    short totalLength = (short) userData.length;
    short bytesRead = 0; // Track the number of bytes read

    // Set the outgoing mode to send data
    apdu.setOutgoing();

    // Loop to send data in chunks
    while (bytesRead < totalLength) {
        // Calculate the chunk size (up to 255 bytes, but no more than the remaining data)
        short chunkSize = (short) (totalLength - bytesRead); // Default to the remaining data
        if (chunkSize > 255) {
            chunkSize = 255; // Limit the chunk size to 255 bytes
        }

        // Set the outgoing length to the chunk size
        apdu.setOutgoingLength(chunkSize);

        // Send the chunk of data
        apdu.sendBytesLong(userData, bytesRead, chunkSize);

        // Update the number of bytes read
        bytesRead += chunkSize;

        // If there is more data to send, throw SW=0x6310 to indicate that more data is available
        if (bytesRead < totalLength) {
            ISOException.throwIt((short) 0x6310); //  continue reading
        }
    }

 
    ISOException.throwIt((short) 0x9000); // Success, all data read
}

	
	
	// PIN 
	private void verifyPIN(APDU apdu) {	
        byte[] buffer = apdu.getBuffer();
		byte pinLength = buffer[ISO7816.OFFSET_LC];

		if (pinLength <= 0 || pinLength > AppletConstants.MAX_PIN_SIZE) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}

		short receivedLength = apdu.setIncomingAndReceive();
		if (receivedLength != pinLength) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}


		if (pin.getTriesRemaining() == (byte) 0x00) {
			ISOException.throwIt((short) 0x6983); 
			return;
		}


		if (pin.check(buffer, ISO7816.OFFSET_CDATA, pinLength)==true) {
			return;
		} else {
			ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
		}
    }



	private void changePin(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		short lc = apdu.setIncomingAndReceive();
        short off = ISO7816.OFFSET_CDATA;
        short expectedLength = (short) (AppletConstants.MAX_PIN_SIZE * 2);
		if (!pin.isValidated()) {
			 ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
			 return;
		}
		if(pin.getTriesRemaining()==0){
			ISOException.throwIt(ISO7816.SW_BYTES_REMAINING_00);
			return;
		}
		
		if (lc != expectedLength) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
		
		// check if bytes are numbers from 0 to 9 
		 for (short i = 0; i < expectedLength ; i++) {
            if (((buffer[(short) (i + off)] < AppletConstants.NUMBER_ZERO)
                    || (buffer[(short) (i + off)] > AppletConstants.NUMBER_NINE))) {
                ISOException.throwIt(ISO7816.SW_WRONG_DATA);
            }
        }
			
		// offset from 0 to MAX_PIN_SIZE is data of current PIN
        if (!pin.check(buffer, off, (byte) AppletConstants.MAX_PIN_SIZE)) {
            
            short triesRemaining = (short) pin.getTriesRemaining();
            ISOException.throwIt((short) (ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED | triesRemaining));
        }
		
		
        pin.update(buffer, (short) (off + AppletConstants.MAX_PIN_SIZE), (byte) AppletConstants.MAX_PIN_SIZE);
		pin.reset();
	}
	
	private void createPIN(APDU apdu){
		byte[] buffer = apdu.getBuffer(); 
		short dataLength = (short) (buffer[ISO7816.OFFSET_LC] & 0xFF);
		if (dataLength == 0 || dataLength > AppletConstants.MAX_PIN_SIZE) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
		
		short receivedLength = apdu.setIncomingAndReceive();
		if (receivedLength != dataLength) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
		
		short dataOffset = ISO7816.OFFSET_CDATA;
		pin.update(buffer, dataOffset, (byte) dataLength);
		
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
