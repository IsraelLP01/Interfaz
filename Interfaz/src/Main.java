// filepath: bodega-objetos-perdidos/src/Main.java
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Establecer el look and feel de la interfaz
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear y mostrar la ventana principal
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}