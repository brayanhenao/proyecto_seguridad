import java.io.File;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class Firmador {

    private PrivateKey clavePrivada;
    private File archivo;

    public Firmador(File archivo, PrivateKey clavePrivada) {
        this.clavePrivada = clavePrivada;
        this.archivo = archivo;
    }

    public String firmar() throws Exception {
        //Firma privada
        Signature firmaPrivada = Signature.getInstance("SHA256withRSA");

        //Se convierte el archivo en un arreglo de bytes para firmarlo.
        byte[] archivoBytes = Files.readAllBytes(archivo.toPath());

        //Se inicia la firma con la clave privada
        firmaPrivada.initSign(clavePrivada);

        //Se firma el archivo
        firmaPrivada.update(archivoBytes);

        //Se guarda la firma
        byte[] signature = firmaPrivada.sign();

        //Se genera la firma
        return Base64.getEncoder().encodeToString(signature);
    }
}
