import java.io.File;
import java.nio.file.Files;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class Verificador {
    private File archivo;
    private String firma;
    private PublicKey clavePublica;

    public Verificador(File archivo, String firma, PublicKey clavePublica) {
        this.archivo = archivo;
        this.firma = firma;
        this.clavePublica = clavePublica;
    }

    /*
     * Se encarga de verificar la firma del emisor del mensaje
     * 
     * @return boolean indicando si la firma del emisor es correcta
     */
    public boolean verificar() throws Exception {

        //Firma pública
        Signature publicSignature = Signature.getInstance("SHA256withRSA");

        //Se convierte el archivo en un arreglo de bytes
        byte[] archivoBytes = Files.readAllBytes(archivo.toPath());

        //Se inicia la verificación con la clave pública
        publicSignature.initVerify(clavePublica);

        //Se actualiza la firma con el archivo
        publicSignature.update(archivoBytes);

        //Se decodifica la firma
        byte[] signatureBytes = Base64.getDecoder().decode(firma);

        //Se verifica la firma
        return publicSignature.verify(signatureBytes);
    }
}
