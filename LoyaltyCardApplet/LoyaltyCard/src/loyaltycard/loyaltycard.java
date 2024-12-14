package loyaltycard;

import javacard.framework.*;


public class loyaltycard extends Applet
{
	private static User user;
	private static OwnerPIN pin;
	private static byte[] userData = new byte[1024];
	private short userDataLength = 0;

 
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		pin = new OwnerPIN(AppletConstants.PIN_RETRIES, AppletConstants.MAX_PIN_SIZE);
		 user = new User();
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
			readUserData(apdu);
			break;
		case AppletInsConstants.INS_WRITE_USER_DATA:
			writeUserData(apdu);
			break;
		case AppletInsConstants.INS_CHANGE_PIN:
			changePin(apdu);
			break;
		case AppletInsConstants.INS_SET_PIN:
			createPIN(apdu);
			break;
			
		case AppletInsConstants.INS_UPDATE_FIRST_NAME:
			updateFirstName(apdu);
			break;
		case AppletInsConstants.INS_UPDATE_LAST_NAME:
			updateLastName(apdu);
			break;
		case AppletInsConstants.INS_UPDATE_BIRTHDAY:
			updateBirthday(apdu);
			break;
		case AppletInsConstants.INS_UPDATE_PHONE:
			updatePhone(apdu);
			break;
		
		
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
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
	
	private void setPIN(byte[] pinData){
		short dataLength = (short) pinData.length;
		if (dataLength == 0 || dataLength > AppletConstants.MAX_PIN_SIZE) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
		pin.update(pinData, (short) 0, (byte) dataLength);
	}

    
    
	// 
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
	
	
	// user data 
	
	private void writeUserData(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    short lc = (short) (buffer[ISO7816.OFFSET_LC] & 0xFF);
	short bytesRead = apdu.setIncomingAndReceive();

    if (bytesRead != lc) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
        // Check if sending data exceeds memory capacity
        if ( lc > userData.length) {
			ISOException.throwIt(ISO7816.SW_FILE_FULL);
		}
		
		// Split the last 6 bytes to call the setPIN function
		short pinOffset = (short) (lc - AppletConstants.MAX_PIN_SIZE);
		byte[] pinData = new byte[AppletConstants.MAX_PIN_SIZE];
		Util.arrayCopy(buffer, (short) (ISO7816.OFFSET_CDATA + pinOffset), pinData, (short) 0, (short) AppletConstants.MAX_PIN_SIZE);
		setPIN(pinData);
		
		userDataLength = (short) (lc - AppletConstants.MAX_PIN_SIZE);
        
        Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, userData, (short) 0, userDataLength);
        parseUserData(userData,userDataLength);
        
	} 

private void parseUserData(byte[] buffer, short bytesRead) {
    short pos = 0;
    byte[] name = {1,2,3,4};

    
    user.setFirstName(parseByteArrayUntilDelimiter(buffer, pos));  
    pos += (short) user.getFirstName().length + 1;  

    user.setLastName(parseByteArrayUntilDelimiter(buffer, pos)); 
    pos += (short) user.getLastName().length + 1; 

    user.setPhone(parseByteArrayUntilDelimiter(buffer, pos));  
    pos += (short) user.getPhone().length + 1;  

    user.setIdentification(parseByteArrayUntilDelimiter(buffer, pos));  // CCCD
    pos += (short) user.getIdentification().length + 1; 

    user.setBirthday(parseByteArrayUntilDelimiter(buffer, pos)); 
    pos += (short) user.getBirthday().length + 1;  

    
    user.setGender(buffer[pos]); 
}


private byte[] parseByteArrayUntilDelimiter(byte[] buffer, short pos) {
    short endPos = pos;
    while (endPos < buffer.length && buffer[endPos] != (byte)0x7C) {
        endPos++;
    }

    // Check if delimiter was found
    if (endPos == buffer.length) {
    	short resultLength = (short) (buffer.length - pos);
        byte[] result = new byte[resultLength];
		Util.arrayCopy(buffer, pos, result, (short) 0, (short) (buffer.length - pos));
		return result;
    }

    short length = (short) (endPos - pos);
    byte[] result = new byte[length];
    Util.arrayCopy(buffer, pos, result, (short) 0, length);
    return result;
}

private void readUserData(APDU apdu) {
    if (user == null) {
        ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
    }

    byte[] buffer = apdu.getBuffer();

   
    sendUserData(apdu);
}

private void sendUserData(APDU apdu) {
    byte[] tempData = new byte[1024];
    short bytesSent = (short) 0; 
	
    
    bytesSent = safeSendFieldData(user.getFirstName(), tempData, bytesSent);
    bytesSent = safeSendFieldData(user.getLastName(), tempData, bytesSent);
    bytesSent = safeSendFieldData(user.getPhone(), tempData, bytesSent);
    bytesSent = safeSendFieldData(user.getIdentification(), tempData, bytesSent);
    bytesSent = safeSendFieldData(user.getBirthday(), tempData, bytesSent);

    
    tempData[bytesSent++] = (byte) (user.getGender());

    
    if (bytesSent > tempData.length) {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID);
    }
    
	 apdu.setOutgoing();
     apdu.setOutgoingLength(bytesSent);
     apdu.sendBytesLong(tempData, (short) 0, bytesSent);
}

private short safeSendFieldData(byte[] fieldData, byte[] tempData, short bytesSent) {
    if (fieldData == null || fieldData.length == 0) {
        
        tempData[bytesSent++] = (byte) '|';
        return bytesSent;
    }

   
    short length = (short) fieldData.length;
   short lengthData = (short) (bytesSent + ((short)length + 1));
    if ( lengthData > tempData.length) {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID);
    }
    Util.arrayCopy(fieldData, (short) 0, tempData, bytesSent, length);
    bytesSent += length;

 
    tempData[bytesSent++] = (byte) '|';
    return bytesSent;
}


	public void updateFirstName(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    short lc = (short) (buffer[ISO7816.OFFSET_LC] & 0xFF);
    short bytesRead = apdu.setIncomingAndReceive();

    
    if (bytesRead != lc) {
        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
    }

    
    // if (lc > userData.length) {
        // ISOException.throwIt(ISO7816.SW_FILE_FULL);
    // }

    
    byte[] data = new byte[lc];
    Util.arrayCopy(buffer, (short) 5, data, (short) 0, lc);

    
    if (data.length > 0 && data[0] != 0) {
        user.setFirstName(data); 
    }
}

public void updateLastName(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    short lc = (short) (buffer[ISO7816.OFFSET_LC] & 0xFF);
    short bytesRead = apdu.setIncomingAndReceive();

   
    if (bytesRead != lc) {
        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
    }

  
    // if (lc > userData.length) {
        // ISOException.throwIt(ISO7816.SW_FILE_FULL);
    // }

    byte[] data = new byte[lc];
    Util.arrayCopy(buffer, (short) 5, data, (short) 0, lc);

    if (data.length > 0 && data[0] != 0) {
        user.setLastName(data);  
    }
}

public void updatePhone(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    short lc = (short) (buffer[ISO7816.OFFSET_LC] & 0xFF);
    short bytesRead = apdu.setIncomingAndReceive();

    if (bytesRead != lc) {
        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
    }

    // if (lc > userData.length) {
        // ISOException.throwIt(ISO7816.SW_FILE_FULL);
    // }

    byte[] data = new byte[lc];
    Util.arrayCopy(buffer, (short) 5, data, (short) 0, lc);

    if (data.length > 0 && data[0] != 0) {
        user.setPhone(data);
    }
}

public void updateBirthday(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    short lc = (short) (buffer[ISO7816.OFFSET_LC] & 0xFF);
    short bytesRead = apdu.setIncomingAndReceive();

    if (bytesRead != lc) {
        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
    }

    // if (lc > userData.length) {
        // ISOException.throwIt(ISO7816.SW_FILE_FULL);
    // }

    byte[] data = new byte[lc];
    Util.arrayCopy(buffer, (short) 5, data, (short) 0, lc);

    if (data.length > 0 && data[0] != 0) {
        user.setBirthday(data);  
    }
}


}



