
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encriptador {

	private String key;
	byte[] inputFile;

	public Encriptador(String passwd, byte[] inputFile) {
		this.inputFile = inputFile;
		key = passwd;

	}

	public byte[] encriptar() {
		return fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile);
	}

	public byte[] desencriptar() {
		return fileProcessor(Cipher.DECRYPT_MODE, key, inputFile);
	}

	private byte[] fileProcessor(int cipherMode, String key, byte[] inputFile) {
		byte[] outputBytes = null;
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(cipherMode, secretKey);

			byte[] inputBytes = inputFile;

			outputBytes = cipher.doFinal(inputBytes);

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return outputBytes;
	}

	public static void main(String[] args) {
		String key = "This is a secret";
		File inputFile = new File("./claves/Priv.key");
		File encryptedFile = new File("./claves/Priv.encrypted");
		File decryptedFile = new File("./claves/Priv2.key");

	}
}
