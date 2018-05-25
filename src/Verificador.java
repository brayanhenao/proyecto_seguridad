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
     * Se encarga de verificar la firma de un archivo.
     *
     * @return boolean indicando si la firma del emisor concuerda con el archivo
     */
    public boolean verificar() throws Exception {

        //Firma pública
        Signature firmador = Signature.getInstance("SHA256withRSA");

        //Se convierte el archivo en un arreglo de bytes
        byte[] archivoBytes = Files.readAllBytes(archivo.toPath());

        //Se inicia la verificación con la clave pública
        firmador.initVerify(clavePublica);

        //Se actualiza la firmador con el archivo
        firmador.update(archivoBytes);

        //Se decodifica la firmador
        byte[] bytesFirma = Base64.getDecoder().decode(this.firma);

        //Se verifica la firmador
        return firmador.verify(bytesFirma);
    }
}
