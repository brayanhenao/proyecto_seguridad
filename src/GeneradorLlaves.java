import java.security.*;

public class GeneradorLlaves {

    KeyPair claves;

    /*
     * Se encarga de generar las llaves (privada y pública)
     */
    private void generarLlaves() throws Exception {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048, new SecureRandom());
        claves = generador.generateKeyPair();
    }

    public GeneradorLlaves() throws Exception {
        generarLlaves();
    }

    /*
     * Se encarga de proporcionar la llave privada al usuario
     *
     * @return PrivateKey,  corresponde a la llave privada
     */
    public PrivateKey getLlavePrivada() {
        return claves.getPrivate();
    }

    /*
     * Se encarga de proporcionar la llave pública al usuario
     *
     * @return PublicKey,  corresponde a la llave pública.
     */
    public PublicKey getLlavePublica() {
        return claves.getPublic();
    }
}
