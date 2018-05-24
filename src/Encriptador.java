
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.Key;

public class Encriptador {

    private String key;
    byte[] inputFile;

    public Encriptador(String passwd, byte[] inputFile) {
        this.inputFile = inputFile;
        key = passwd;

    }

    public byte[] encriptar() throws Exception {
        return fileProcessor(Cipher.ENCRYPT_MODE, key, inputFile);
    }

    public byte[] desencriptar() throws Exception {
        return fileProcessor(Cipher.DECRYPT_MODE, key, inputFile);
    }

    private byte[] fileProcessor(int cipherMode, String key, byte[] inputFile) throws Exception {
        byte[] outputBytes = null;

        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        byte[] inputBytes = inputFile;

        outputBytes = cipher.doFinal(inputBytes);


        return outputBytes;
    }

    public static void main(String[] args) {
        String key = "This is a secret";
        File inputFile = new File("./claves/Priv.key");
        File encryptedFile = new File("./claves/Priv.encrypted");
        File decryptedFile = new File("./claves/Priv2.key");

    }
}
