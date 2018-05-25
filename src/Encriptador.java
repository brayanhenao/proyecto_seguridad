
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class Encriptador {

    private String key;
    byte[] inputFile;

    public Encriptador(String passwd, byte[] inputFile) {
        this.inputFile = inputFile;
        key = passwd;
    }

    /*
     * Se encarga de encriptar el inputFile que tiene como atributo la instancia
     *
     * @return arreglo de bytes que corresponde al texto encriptado del inputFile
     */
    public byte[] encriptar() throws Exception {
        return encriptador(Cipher.ENCRYPT_MODE, key, inputFile);
    }

    /*
     * Se encarga de desencriptar el inputFile que tiene como atributo la instancia
     *
     * @return arreglo de bytes que corresponde al texto original del inputFile
     */
    public byte[] desencriptar() throws Exception {
        return encriptador(Cipher.DECRYPT_MODE, key, inputFile);
    }

    /*
     * Se encarga de procesar el archivo que tiene como parametro seg�n la operaci�n que se
     * indique por medio del parametro cipheMode
     *
     * @param modo. Indica la operaci�n a realizar (Encriptar o desencriptar)
     * @param Key. Clave para realizar la operaci�n
     * @param archivo. Archivo original el cu�l se va a procesar
     * @return arreglo de bytes que corresponde al texto original del archivo
     */
    private byte[] encriptador(int modo, String key, byte[] archivo) throws Exception {
        byte[] outputBytes = null;

        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(modo, secretKey);

        byte[] inputBytes = archivo;

        outputBytes = cipher.doFinal(inputBytes);

        return outputBytes;
    }
}
