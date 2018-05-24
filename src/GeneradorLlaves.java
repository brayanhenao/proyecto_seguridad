import java.security.*;

public class GeneradorLlaves {

    KeyPair claves;

    private void generarLlaves() throws Exception {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048, new SecureRandom());
        claves = generador.generateKeyPair();
    }

    public GeneradorLlaves() throws Exception {
        generarLlaves();
    }

    public PrivateKey getLlavePrivada() {
        return claves.getPrivate();
    }

    public PublicKey getLlavePublica() {
        return claves.getPublic();
    }
}
