import javax.swing.*;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ajustar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurar los botones de diálogo en español
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");

        SwingUtilities.invokeLater(() -> {
            try {
                                // Conectar a la base de datos MySQL de XAMPP
                String dbUrl = "jdbc:mysql://localhost:3306/objetosdb"; // Asegúrate de que la base de datos 'objetosdb' exista
                String user = "root";
                String password = ""; // Por defecto, XAMPP no tiene contraseña para root

                DatabaseManager dbManager = new DatabaseManager(dbUrl, user, password);
                MainFrame mainFrame = new MainFrame(dbManager);
                mainFrame.refreshObjectList(); // mostrar lista inicial
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al inicializar la aplicación: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
