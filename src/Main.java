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

        // Conectar a la base de datos MySQL de XAMPP
        String dbUrl = "jdbc:mysql://localhost:3306/objetosdb"; // Asegúrate de que la base de datos 'objetosdb' exista
        String user = "root";
        String password = ""; // Por defecto, XAMPP no tiene contraseña para root

        DatabaseManager dbManager = new DatabaseManager(dbUrl, user, password);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(dbManager);
            mainFrame.setVisible(true);
            mainFrame.refreshObjectList(); // mostrar lista inicial
        });
    }
}
