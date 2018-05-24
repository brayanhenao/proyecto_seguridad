import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;

public class Pruebita {

    public static void main(String[] args) throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            PrintWriter pw = new PrintWriter(new File(path));
            pw.close();
        }

    }
}
