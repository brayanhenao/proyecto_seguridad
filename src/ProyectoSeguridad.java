import javax.crypto.BadPaddingException;
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
            String op = JOptionPane.showInputDialog(null,
                    "¿QUE DESEA HACER? SELECCIONE UNA OPCION\n 1- GENERAR LLAVES"
                            + " \n 2- FIRMAR UN ARCHIVO\n 3- VERIFICAR LA FIRMA DE UN ARCHIVO\n 4- SALIR\n");
            if (op == null) {
                break;
            }
            try {
                System.out.println(op);
                opcion = Integer.parseInt(op);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "INGRESE UNA OPCION VALIDA", "ERROR", JOptionPane.ERROR_MESSAGE);
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
                case JOptionPane.CLOSED_OPTION:
                    opcion = 4;
            }

        } while (opcion != 4);
    }

    /*
     * Se encarga de iniciar el proceso de generacion de claves
     */
    private static void generador() throws Exception {
        GeneradorLlaves llaves = new GeneradorLlaves();

        String pass = JOptionPane.showInputDialog(null, "DIGITE UNA PASSWORD PARA PROTEGER" +
                "SU CLAVE PRIVADA");

        pass = formatPasswd(pass);

        System.out.println("**************GENERANDO LLAVES*************");

        int opcion = 0;
        do {
            String op = JOptionPane.showInputDialog(null, "¿QUE DESEA HACER? SELECCIONE UNA "
                    + "OPCION\n 1- GUARDAR LLAVE PUBLICA\n 2- GUARDAR LLAVE PRIVADA\n 3- SALIR\n");

            if (op == null) {
                break;
            }
            try {
                opcion = Integer.parseInt(op);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "INGRESE UNA OPCION VALIDA",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            int seleccion;
            JFileChooser jf = new JFileChooser("claves/");

            String path;
            switch (opcion) {
                case 1:
                    jf.setDialogTitle("GUARDAR LLAVE PUBLICA");
                    seleccion = jf.showSaveDialog(null);
                    if (seleccion == JFileChooser.APPROVE_OPTION) {
                        path = jf.getSelectedFile().getAbsolutePath();
                        FileOutputStream out = new FileOutputStream(new File(path + ".pub"));
                        out.write(llaves.getLlavePublica().getEncoded());
                        out.close();
                    }
                    System.err.println("Formato Publica: " + llaves.getLlavePublica().getFormat());
                    continue;
                case 2:
                    jf.setDialogTitle("GUARDAR LLAVE PRIVADA");
                    seleccion = jf.showSaveDialog(null);
                    if (seleccion == JFileChooser.APPROVE_OPTION) {
                        path = jf.getSelectedFile().getAbsolutePath();

                        FileOutputStream out = new FileOutputStream(new File(path + ".key"));
                        Encriptador h = new Encriptador(pass, llaves.getLlavePrivada().getEncoded());
                        out.write(h.encriptar());
                        out.close();
                    }
                    System.err.println("Formato Privada: " + llaves.getLlavePrivada().getFormat());
                    continue;
                case JOptionPane.CLOSED_OPTION:
                    opcion = 3;
            }

        } while (opcion != 3);

    }

    /*
     * Se encarga de iniciar el firmar un archivo
     */
    private static void firmador() throws Exception, BadPaddingException {
        JOptionPane.showMessageDialog(null, "SELECCIONE EL ARCHIVO A FIRMAR");
        JFileChooser jf = new JFileChooser();
        jf.setCurrentDirectory(new File("archivos/"));
        jf.setDialogTitle("SELECCIONAR ARCHIVO A FIRMAR");
        int seleccion = jf.showSaveDialog(null);
        File archivo = null;
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivo = jf.getSelectedFile();
        }

        JOptionPane.showMessageDialog(null, "SELECCIONE LA LLAVE PRIVADA PARA FIRMAR");
        jf.setCurrentDirectory(new File("claves/"));
        jf.setDialogTitle("SELECCIONAR LLAVE PRIVADA");
        seleccion = jf.showSaveDialog(null);
        boolean ok = true;
        PrivateKey privateKey = null;
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            Path path = Paths.get(jf.getSelectedFile().toURI());
            byte[] bytes = Files.readAllBytes(path);
            String pass = JOptionPane.showInputDialog(null, "DIGITE EL PASSWORD ASOCIADO A" +
                    "ESTA CLAVE PRIVADA");

            pass = formatPasswd(pass);

            Encriptador enc = new Encriptador(pass, bytes);
            try {
                bytes = enc.desencriptar();
            } catch (BadPaddingException e) {
                ok = false;

                JOptionPane.showMessageDialog(null, "PASSWORD INCORRECTO",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (ok) {
                PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                privateKey = kf.generatePrivate(ks);
            }
        }

        if (ok) {
            Firmador firmador = new Firmador(archivo, privateKey);

            JOptionPane.showMessageDialog(null, "GUARDE SU FIRMA");
            jf.setCurrentDirectory(new File("archivos/"));
            jf.setDialogTitle("GUARDAR FIRMA");
            seleccion = jf.showSaveDialog(null);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                String path = jf.getSelectedFile().getAbsolutePath();
                PrintWriter out = new PrintWriter(new File(path + ".txt"));
                out.write(firmador.firmar());
                out.close();
            }
        }
    }

    /*
     * Se encarga de iniciar el proceso de verificacion de firmas
     */
    private static void verificador() throws Exception {
        JOptionPane.showMessageDialog(null, "SELECCIONE EL ARCHIVO A VERIFICAR");
        JFileChooser jf = new JFileChooser();
        jf.setCurrentDirectory(new File("archivos/"));
        jf.setDialogTitle("SELECCIONAR ARCHIVO A VERIFICAR");
        int seleccion = jf.showSaveDialog(null);
        File archivo = null;
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivo = jf.getSelectedFile();
        }

        JOptionPane.showMessageDialog(null, "SELECCIONE LA LLAVE PUBLICA");
        jf.setCurrentDirectory(new File("claves/"));
        jf.setDialogTitle("SELECCIONAR LLAVE PUBLICA");
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
        jf.setCurrentDirectory(new File("archivos/"));
        jf.setDialogTitle("SELECCIONAR FIRMA");
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
            JOptionPane.showMessageDialog(null, "LA FIRMA ES INCORRECTA, ARCHIVO ALTERADO O" +
                    "INCORRECTO", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Se encarga de arreglar el formato del password ingresado.
     *
     * @return string, passwd del usuario ajustado al formado 16bytes AES
     */
    private static String formatPasswd(String passwd) {
        String finalPass = passwd;
        if (passwd.length() < 16) {
            for (int i = passwd.length(); i < 16; i++) {
                finalPass += '0';
            }
        } else {
            finalPass = passwd.substring(0, 15);
        }
        return finalPass;
    }
}
