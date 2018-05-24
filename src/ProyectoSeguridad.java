import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class ProyectoSeguridad {


    public static void main(String... argv) throws Exception {

        System.out.println("****************************************************************************");
        System.out.println("****************************************************************************");
        System.out.println("*******************GENERADOR, FIRMADOR Y VERIFICADOR***********************");
        System.out.println("********HECHO POR: -BRAYAN HENAO - SERGIO OSPINA - ESTEBAN AGUIRRE**********");
        System.out.println("****************************************************************************");
        System.out.println("****************************************************************************");
        int opcion = 0;
        do {
            String op = JOptionPane.showInputDialog(null, "¿Qué desea hacer? Seleccione una opción\n 1- Generar llaves" +
                    " \n 2- Firmar un archivo\n 3- Verificar la firma de un archivo\n 4- Salir\n");

            try {
                opcion = Integer.parseInt(op);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "INGRESE UNA OPCIÓN VÁLIDA", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            switch (opcion) {
                case 1:
                    generador();
                    continue;
                case 2:
                    firmador();
                    continue;
                case 3:
                    verificador();
                    continue;
                default:
                    continue;

            }

        } while (opcion != 4);


        //KeyPair pair = generarLlaves();
        //KeyPair pair = getKeyPairFromKeyStore();
        //Our secret message
        String message = "the answer to life the universe and everything";
        //Let's sign our message
        //String signature = sign("foobar", pair.getPrivate());
        //Let's check the signature
        // boolean isCorrect = verify("foobar", signature, pair.getPublic());
        //System.out.println("Signature correct: " + isCorrect);
    }

    public static void generador() throws Exception {
        GeneradorLlaves llaves = new GeneradorLlaves();
        System.out.println("**************GENERANDO LLAVES*************");

        int opcion = 0;
        do {
            String op = JOptionPane.showInputDialog(null, "¿Qué desea hacer? Seleccione una " +
                    "opción\n 1- Guardar llave pública\n 2- Guardar llave privada\n 3- Salir\n");
            try {
                opcion = Integer.parseInt(op);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "INGRESE UNA OPCIÓN VÁLIDA", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            int seleccion;
            JFileChooser jf = new JFileChooser();
            String path;
            switch (opcion) {
                case 1:
                    jf.setDialogTitle("Guardar llave pública.");
                    seleccion = jf.showSaveDialog(null);
                    if (seleccion == JFileChooser.APPROVE_OPTION) {
                        path = jf.getSelectedFile().getAbsolutePath();
                        FileOutputStream out = new FileOutputStream(new File(path + ".pub"));
                        out.write(llaves.getLlavePublica().getEncoded());
                        out.close();
                    }
                    System.err.println("Private key format: " + llaves.getLlavePublica().getFormat());
                    continue;
                case 2:
                    jf.setDialogTitle("Guardar llave privada.");
                    seleccion = jf.showSaveDialog(null);
                    if (seleccion == JFileChooser.APPROVE_OPTION) {
                        path = jf.getSelectedFile().getAbsolutePath();
                        FileOutputStream out = new FileOutputStream(new File(path + ".key"));
                        out.write(llaves.getLlavePrivada().getEncoded());
                        out.close();
                    }
                    System.err.println("Private key format: " + llaves.getLlavePrivada().getFormat());
                    continue;
                default:
                    continue;
            }

        } while (opcion != 3);


    }

    public static void firmador() throws Exception {
        JOptionPane.showMessageDialog(null, "SELECCIONE EL ARCHIVO A FIRMAR");
        JFileChooser jf = new JFileChooser();
        jf.setDialogTitle("Seleccionar archivo a firmar");
        int seleccion = jf.showSaveDialog(null);
        File archivo = null;
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivo = jf.getSelectedFile();
        }

        JOptionPane.showMessageDialog(null, "SELECCIONE LA LLAVE PRIVADA PARA FIRMAR");
        jf.setDialogTitle("Seleccionar llave privada");
        seleccion = jf.showSaveDialog(null);

        PrivateKey privateKey = null;
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            Path path = Paths.get(jf.getSelectedFile().toURI());
            byte[] bytes = Files.readAllBytes(path);

            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(ks);
        }
        Firmador firmador = new Firmador(archivo, privateKey);
        jf.setDialogTitle("Guardar firma");
        seleccion = jf.showSaveDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String path = jf.getSelectedFile().getAbsolutePath();
            PrintWriter out = new PrintWriter(new File(path + ".txt"));
            out.write(firmador.firmar());
            out.close();
        }

    }

    public static void verificador() throws Exception {
        JOptionPane.showMessageDialog(null, "SELECCIONE EL ARCHIVO A VERIFICAR");
        JFileChooser jf = new JFileChooser();
        jf.setDialogTitle("Seleccionar archivo a verificar");
        int seleccion = jf.showSaveDialog(null);
        File archivo = null;
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivo = jf.getSelectedFile();
        }

        JOptionPane.showMessageDialog(null, "SELECCIONE LA LLAVE PUBLICA");
        jf.setDialogTitle("Seleccionar llave pública");
        seleccion = jf.showSaveDialog(null);

        PublicKey publicKey = null;
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            Path path = Paths.get(jf.getSelectedFile().toURI());
            byte[] bytes = Files.readAllBytes(path);

            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(ks);
        }

        JOptionPane.showMessageDialog(null, "SELECCIONE LA FIRMA DEL ARCHIVO");
        jf.setDialogTitle("Seleccionar firma");
        seleccion = jf.showSaveDialog(null);

        String firma = "";
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File firmita = jf.getSelectedFile();
            BufferedReader in = new BufferedReader(new FileReader(firmita));
            firma = in.readLine();
            in.close();
        }

        Verificador verificador = new Verificador(archivo, firma, publicKey);
        if (verificador.verificar()) {
            JOptionPane.showMessageDialog(null, "LA FIRMA ES CORRECTA");
        } else {
            JOptionPane.showMessageDialog(null, "LA FIRMA ES INCORRECTA", "ERROR", JOptionPane.ERROR_MESSAGE);
        }


    }
}
