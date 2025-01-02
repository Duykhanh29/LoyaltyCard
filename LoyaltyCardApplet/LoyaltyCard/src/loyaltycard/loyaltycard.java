package loyaltycard;

import javacard.framework.*;
import javacard.security.*;
import javacardx.crypto.Cipher;

public class loyaltycard extends Applet {
	private static User user;
	private static OwnerPIN pin;
	private static byte[] userData = new byte[1024];
	private short userDataLength = 0;
	private static MessageDigest digest;
	private AESKey aesKey;
	private static Cipher cipher;
	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;

	public static void install(byte[] bArray, short bOffset, byte bLength) {
		pin = new OwnerPIN(AppletConstants.PIN_RETRIES, AppletConstants.MAX_PIN_SIZE);
		user = new User();
		byte[] pinArr = AppletConstants.DEFAUL_PIN;
		pin.update(pinArr, (short) 0, (byte) pinArr.length);

		cipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_ECB_NOPAD, false);
		digest = MessageDigest.getInstance(MessageDigest.ALG_SHA, false); // ALG_SHA returns with 20 byte
		new loyaltycard().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu) {
		if (selectingApplet()) {
			return;
		}

		byte[] buf = apdu.getBuffer();
		byte ins = buf[ISO7816.OFFSET_INS];
		switch (ins) {
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
			case AppletInsConstants.INS_CHECK_INIT:
				checkCardHasData(apdu);
				break;
			case AppletInsConstants.INS_GET_PUBLIC_KEY:
				getPublicKey(apdu);
				break;
			case AppletInsConstants.INS_SIGN_DATA:
				signData(apdu);
				break;
				
			case AppletInsConstants.INS_UNLOCK_PIN:
				unlockPIN(apdu);
				break;
	
		case AppletInsConstants.INS_UPDATE_POINT:
			updatePoints(apdu);
			break;
		
			default:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	
	private void signData(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		short dataLen = apdu.setIncomingAndReceive(); // Nhn d liu t APDU (chui ngu nhi�n)
		
		// Kim tra k�ch thc d liu
		if (dataLen > 64) { // Gii hn d liu gi v�o (t�y thuc v�o thut to�n k�)
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
	
		try {
			// To i tng Signature  k� d liu
			Signature signature = Signature.getInstance(Signature.ALG_RSA_SHA_PKCS1, false);
			signature.init(privateKey, Signature.MODE_SIGN);
			
			// K� d liu
			short signLen = signature.sign(buffer, ISO7816.OFFSET_CDATA, dataLen, buffer, (short) 0);
	
			// Gi ch k� v client
			apdu.setOutgoing();
			apdu.setOutgoingLength(signLen);
			apdu.sendBytes((short) 0, signLen);
		} catch (CryptoException e) {
			ISOException.throwIt(ISO7816.SW_UNKNOWN);
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

		if (pin.check(buffer, ISO7816.OFFSET_CDATA, pinLength) == true) {
			return;
		} else {
			 short remainingTries = pin.getTriesRemaining();
			// Returns the error code with the remaining number of times the PIN is entered incorrectly
			ISOException.throwIt((short) (0x63C0 | remainingTries)); 
			// ISOException.throwIt(errorCode);
			
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

		if (pin.getTriesRemaining() == 0) {
			ISOException.throwIt(ISO7816.SW_BYTES_REMAINING_00);
			return;
		}

		if (lc != expectedLength) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}

		// check if bytes are numbers from 0 to 9
		for (short i = 0; i < expectedLength; i++) {
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

		try {
			JCSystem.beginTransaction();

			// Step 1: Store the old AES key
			// byte[] oldAesKey = new byte[(short)16];
			// aesKey.getKey(oldAesKey, (short) 0);

			// Step 2: Decrypt user data fields using the old AES key
			short firstNameLen = (short) user.getFirstName().length;
			short lastNameLen = (short) user.getLastName().length;
			short birthdayLen = (short) user.getBirthday().length;
			short phoneLen = (short) user.getPhone().length;
			short identificationLen = (short) user.getIdentification().length;

			byte[] firstName = new byte[firstNameLen];
			byte[] lastName = new byte[lastNameLen];
			byte[] birthday = new byte[birthdayLen];
			byte[] phone = new byte[phoneLen];
			byte[] identification = new byte[identificationLen];
			Util.arrayCopy(user.getFirstName(), (short) (0), firstName, (short) 0, (short) firstNameLen);
			Util.arrayCopy(user.getLastName(), (short) (0), lastName, (short) 0, (short) lastNameLen);
			Util.arrayCopy(user.getBirthday(), (short) (0), birthday, (short) 0, (short) birthdayLen);
			Util.arrayCopy(user.getPhone(), (short) (0), phone, (short) 0, (short) phoneLen);
			Util.arrayCopy(user.getIdentification(), (short) (0), identification, (short) 0, (short) identificationLen);

			byte[] decryptedFirstName = decryptField(firstName);
			byte[] decryptedLastName = decryptField(lastName);
			byte[] decryptedPhone = decryptField(phone);
			byte[] decryptedIdentification = decryptField(identification);
			byte[] decryptedBirthday = decryptField(birthday);

			// Step 3: Change the PIN and update the AES key
			short dataLength = (short) AppletConstants.MAX_PIN_SIZE;
			byte[] pinData = new byte[dataLength];
			Util.arrayCopy(buffer, (short) (off + dataLength), pinData, (short) 0, (short) dataLength);
			pin.update(pinData, (short) 0, (byte) dataLength);
			pin.reset();
			if (!pin.check(pinData, (short) 0, (byte) dataLength)) {
				ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
			}
			generateAESKeyFromPin(pinData); // Update AES key

			// Step 4: Encrypt user data fields using the new AES key
			user.setFirstName(encryptField(decryptedFirstName));
			user.setLastName(encryptField(decryptedLastName));
			user.setPhone(encryptField(decryptedPhone));
			user.setIdentification(encryptField(decryptedIdentification));
			user.setBirthday(encryptField(decryptedBirthday));

			JCSystem.commitTransaction();
		} catch (Exception e) {
			JCSystem.abortTransaction();
			ISOException.throwIt(AppletConstants.SW_ACTION_FAILED);
		}
	}

	private void createPIN(APDU apdu) {
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
		pin.reset();
	}

	private void setPIN(byte[] pinData) {
		short dataLength = (short) pinData.length;
		if (dataLength == 0 || dataLength > AppletConstants.MAX_PIN_SIZE) {
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		}
		pin.update(pinData, (short) 0, (byte) dataLength);
		pin.reset();
		if (!pin.check(pinData, (short) 0, (byte) dataLength)) {
			ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
		}
		generateAESKeyFromPin(pinData);

	}
	
	
	private void unlockPIN(APDU apdu) {
		byte[] buffer = apdu.getBuffer();
		short lc = apdu.setIncomingAndReceive();
		short off = ISO7816.OFFSET_CDATA;
		pin.resetAndUnblock();
	}
	
	

	private byte[] generateAESKeyFromPin(byte[] pinData) {
		try {

			byte[] hashedPin = new byte[digest.getLength()];
			digest.doFinal(pinData, (short) 0, (short) pinData.length, hashedPin, (short) 0);

			byte[] keyBytes = new byte[16];

			Util.arrayCopy(hashedPin, (short) 0, keyBytes, (short) 0, (short) 16);

			aesKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES, (short) (8 * 16), false);
			aesKey.setKey(keyBytes, (short) 0);

			return keyBytes;
		} catch (Exception e) {

			ISOException.throwIt(ISO7816.SW_UNKNOWN);
			return null;
		}
	}

	//
	public boolean select() {
		if (pin.getTriesRemaining() == 0)
			return false;

		if (pin.getTriesRemaining() == 0)
			return false;

		return true;
	}

	public void deselect() {
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
		if (lc > userData.length) {
			ISOException.throwIt(ISO7816.SW_FILE_FULL);
		}

		JCSystem.beginTransaction();

		try {
			// Split the last 6 bytes to call the setPIN function
			short pinOffset = (short) (lc - AppletConstants.MAX_PIN_SIZE);
			byte[] pinData = new byte[AppletConstants.MAX_PIN_SIZE];
			Util.arrayCopy(buffer, (short) (ISO7816.OFFSET_CDATA + pinOffset), pinData, (short) 0,
					(short) AppletConstants.MAX_PIN_SIZE);
			setPIN(pinData);

			userDataLength = (short) (lc - AppletConstants.MAX_PIN_SIZE);
			Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, userData, (short) 0, userDataLength);
			parseUserData(userData, userDataLength);
			innitKeyRSA();

			JCSystem.commitTransaction();

		} catch (ISOException e) {

			JCSystem.abortTransaction();
			ISOException.throwIt((short) AppletConstants.SW_ACTION_FAILED);
		}

	}

	private void innitKeyRSA() {
		try {
			KeyPair keyPair = new KeyPair(KeyPair.ALG_RSA, KeyBuilder.LENGTH_RSA_1024);
			keyPair.genKeyPair();

			// Lu private key trong th
			this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
			this.publicKey = (RSAPublicKey) keyPair.getPublic();
		} catch (CryptoException e) {
			JCSystem.abortTransaction();
			ISOException.throwIt(ISO7816.SW_UNKNOWN);
		}
	}

	private void parseUserData(byte[] buffer, short bytesRead) {
		short pos = 0;
		byte[] name = { 1, 2, 3, 4 };
		
		// Parse userId (short)
		short userId = Util.getShort(buffer, pos);
		user.setID(userId);
		pos += 2; // short takes 2 bytes

		// Ignore the '|' separator
		pos += 1;
		
		byte[] firstName = parseByteArrayUntilDelimiter(buffer, pos);
		byte[] firstNAME = encryptField(firstName);
		user.setFirstName(firstNAME);
		pos += (short) firstName.length + 1;

		byte[] lastName = parseByteArrayUntilDelimiter(buffer, pos);
		user.setLastName(encryptField(lastName));
		pos += (short) lastName.length + 1;

		byte[] phone = parseByteArrayUntilDelimiter(buffer, pos);
		user.setPhone(encryptField(phone));
		pos += (short) phone.length + 1;

		byte[] identification = parseByteArrayUntilDelimiter(buffer, pos);
		user.setIdentification(encryptField(identification)); // CCCD
		pos += (short) identification.length + 1;

		byte[] birthday = parseByteArrayUntilDelimiter(buffer, pos);
		user.setBirthday(encryptField(birthday));
		pos += (short) birthday.length + 1;

		user.setGender(buffer[pos]); // Do not encode single byte fields
	}

	private byte[] parseByteArrayUntilDelimiter(byte[] buffer, short pos) {
		short endPos = pos;
		while (endPos < buffer.length && buffer[endPos] != (byte) 0x7C) {
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

	private byte[] encryptField(byte[] fieldData) {

		short blockSize = 16;
		short paddingLength = (short) (blockSize - (fieldData.length % blockSize));
		short paddedLength = (short) (fieldData.length + paddingLength);

		byte[] paddedData = new byte[paddedLength];
		Util.arrayCopy(fieldData, (short) 0, paddedData, (short) 0, (short) fieldData.length);

		for (short i = (short) fieldData.length; i < paddedLength; i++) {
			paddedData[i] = (byte) paddingLength;
		}

		byte[] encryptedData = new byte[paddedLength];

		cipher.init(aesKey, Cipher.MODE_ENCRYPT);
		cipher.doFinal(paddedData, (short) 0, paddedLength, encryptedData, (short) 0);

		return encryptedData;
	}

	// read data

	private void readUserData(APDU apdu) {
		if (user == null) {
			ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
		}

		byte[] buffer = apdu.getBuffer();

		sendUserData(apdu);
	}

	private void getPublicKey(APDU apdu) {
		if (user == null) {
			ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
		}

		byte[] buffer = apdu.getBuffer();

		sendPublicKey(apdu);
	}

	private void sendUserData(APDU apdu) {
		byte[] tempData = new byte[1024];
		short bytesSent = (short) 0;
		
		short id = user.getId();
		Util.setShort(tempData, bytesSent, id);
		bytesSent += 2; 
		
		bytesSent = safeSendFieldData(user.getFirstName(), tempData, bytesSent);
		bytesSent = safeSendFieldData(user.getLastName(), tempData, bytesSent);
		bytesSent = safeSendFieldData(user.getPhone(), tempData, bytesSent);
		bytesSent = safeSendFieldData(user.getIdentification(), tempData, bytesSent);
		bytesSent = safeSendFieldData(user.getBirthday(), tempData, bytesSent);

		tempData[bytesSent++] = (byte) (user.getGender());


		tempData[bytesSent++] = (byte) '|';
		
		short point = user.getPoint();
		Util.setShort(tempData, bytesSent, point);
		bytesSent += 2; 
		
		if (bytesSent > tempData.length) {
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}

		apdu.setOutgoing();
		apdu.setOutgoingLength(bytesSent);
		apdu.sendBytesLong(tempData, (short) 0, bytesSent);
	}

	private void sendPublicKey(APDU apdu) {
		byte[] tempData = new byte[1024];
		short bytesSent = (short) 0;

		byte[] publicKeyBytes = getPublicKeyBytes(publicKey);
		if (publicKeyBytes != null) {
			short publicKeyLength = (short) publicKeyBytes.length;
			if ((short) (bytesSent + publicKeyLength) > tempData.length) {
				ISOException.throwIt(ISO7816.SW_DATA_INVALID); // Kim tra tr�n b m
			}
			Util.arrayCopy(publicKeyBytes, (short) 0, tempData, bytesSent, publicKeyLength);
			bytesSent += publicKeyLength;
		}

		if (bytesSent > tempData.length) {
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}

		apdu.setOutgoing();
		apdu.setOutgoingLength(bytesSent);
		apdu.sendBytesLong(tempData, (short) 0, bytesSent);
	}

	private byte[] getPublicKeyBytes(RSAPublicKey rsaPublicKey) {
		byte[] publicKeyBytes = new byte[128 + 3];
		short bytesWritten = 0;

		byte[] modulus = new byte[128];
		rsaPublicKey.getModulus(modulus, (short) 0);

		Util.arrayCopy(modulus, (short) 0, publicKeyBytes, bytesWritten, (short) modulus.length);
		bytesWritten += modulus.length;

		byte[] exponent = new byte[3];
		rsaPublicKey.getExponent(exponent, (short) 0);

		Util.arrayCopy(exponent, (short) 0, publicKeyBytes, bytesWritten, (short) exponent.length);
		bytesWritten += exponent.length;

		byte[] finalPublicKeyBytes = new byte[bytesWritten];
		Util.arrayCopy(publicKeyBytes, (short) 0, finalPublicKeyBytes, (short) 0, bytesWritten);

		return finalPublicKeyBytes;
	}

	private short safeSendFieldData(byte[] fieldData, byte[] tempData, short bytesSent) {
		if (fieldData == null || fieldData.length == 0) {

			tempData[bytesSent++] = (byte) '|';
			return bytesSent;
		}

		byte[] decryptedData = decryptField(fieldData);
		short length = (short) decryptedData.length;
		if ((short) (bytesSent + length + 1) > tempData.length) {
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}
		Util.arrayCopy(decryptedData, (short) 0, tempData, bytesSent, length);
		bytesSent += length;

		tempData[bytesSent++] = (byte) '|';
		return bytesSent;
	}

	private byte[] decryptField(byte[] encryptedData) {

		byte[] decryptedData = new byte[encryptedData.length];

		cipher.init(aesKey, Cipher.MODE_DECRYPT);
		cipher.doFinal(encryptedData, (short) 0, (short) encryptedData.length, decryptedData, (short) 0);

		short paddingLength = (short) (decryptedData[(short) (decryptedData.length - 1)] & 0xFF);
		if (paddingLength < 1 || paddingLength > 16) {
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}

		short dataLength = (short) (decryptedData.length - paddingLength);
		byte[] originalData = new byte[dataLength];
		Util.arrayCopy(decryptedData, (short) 0, originalData, (short) 0, dataLength);

		return originalData;
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

		JCSystem.beginTransaction();
		try {
			if (data.length > 0 && data[0] != 0) {
				byte[] encryptedData = encryptField(data);
				user.setFirstName(encryptedData);
			}
			JCSystem.commitTransaction();
		} catch (Exception e) {
			JCSystem.abortTransaction();
			ISOException.throwIt((short) AppletConstants.SW_ACTION_FAILED);
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

		JCSystem.beginTransaction();
		try {
			if (data.length > 0 && data[0] != 0) {
				byte[] encryptedData = encryptField(data);
				user.setLastName(encryptedData);
			}
			JCSystem.commitTransaction();
		} catch (Exception e) {
			JCSystem.abortTransaction();
			ISOException.throwIt((short) AppletConstants.SW_ACTION_FAILED);
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

		JCSystem.beginTransaction();
		try {
			if (data.length > 0 && data[0] != 0) {
				byte[] encryptedData = encryptField(data);
				user.setPhone(encryptedData);
			}
			JCSystem.commitTransaction();
		} catch (Exception e) {
			JCSystem.abortTransaction();
			ISOException.throwIt((short) AppletConstants.SW_ACTION_FAILED);
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
		JCSystem.beginTransaction();
		try {
			if (data.length > 0 && data[0] != 0) {
				byte[] encryptedData = encryptField(data);
				user.setBirthday(encryptedData);
			}
			JCSystem.commitTransaction();
		} catch (Exception e) {
			JCSystem.abortTransaction();
			ISOException.throwIt((short) AppletConstants.SW_ACTION_FAILED);
		}

	}

	public void checkCardHasData(APDU apdu) {
		short bytesRead = apdu.setIncomingAndReceive();
		if (user.getBirthday() == null && user.getFirstName() == null
				&& user.getGender() == 0 && user.getIdentification() == null
				&& user.getPhone() == null && user.getLastName() == null) {
			// card has no data
			ISOException.throwIt((short) AppletConstants.NO_EXIST_DATA);
		}
		// ISOException.throwIt(ISO7816.SW_NO_ERROR);
	}
	
	
	
	private void updatePoints(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short bytesRead = apdu.setIncomingAndReceive();
        
        byte p1 = buffer[ISO7816.OFFSET_P1];
        
        
        short pointsToUpdate = (short) ((buffer[ISO7816.OFFSET_CDATA] << 8) | (buffer[ISO7816.OFFSET_CDATA + 1] & 0xFF));

        switch (p1) {
            case AppletInsConstants.P1_PLUS_POINT:
                user.addPoint(pointsToUpdate); 
                break;
            case AppletInsConstants.P1_SUB_POINT:
                user.subtractPoint(pointsToUpdate); 
                break;
            default:
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
        }

        
        if (user.getPoint() < 0) {
            user.setPoint((short) 0);
        }

    } 
	
	
	

}
