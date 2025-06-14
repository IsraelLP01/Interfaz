import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    private JList<String> objectList;
    private DefaultListModel<String> listModel;
    private JLabel imageLabel;
    private DatabaseManager dbManager;

    public MainFrame(DatabaseManager dbManager) {
        this.dbManager = dbManager;

        setTitle("Sistema de Objetos Perdidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();
    }

    private void initializeUI() {
        // Create the list model and JList
        listModel = new DefaultListModel<>();
        objectList = new JList<>(listModel);
        objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(objectList);
        listScrollPane.setPreferredSize(new Dimension(300, 400));

        // Create image display area
        imageLabel = new JLabel("Seleccione un objeto para ver su imagen", JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 400));
        imageLabel.setBorder(BorderFactory.createEtchedBorder());
        imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        imageLabel.setHorizontalTextPosition(JLabel.CENTER);

        // Add selection listener to display the image when an item is selected
        objectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedImage();
            }
        });

        // Create buttons
        JButton addButton = new JButton("Agregar Objeto");
        JButton deleteButton = new JButton("Eliminar Objeto");
        JButton searchButton = new JButton("Buscar Objeto");

        // Set button actions
        addButton.addActionListener(e -> {
            AddObjectDialog dialog = new AddObjectDialog(this);
            dialog.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            DeleteObjectDialog dialog = new DeleteObjectDialog(this);
            dialog.setVisible(true);
        });

        searchButton.addActionListener(e -> {
            SearchObjectDialog dialog = new SearchObjectDialog(this);
            dialog.setVisible(true);
        });

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        // Create split pane to divide list and image
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                listScrollPane,
                new JScrollPane(imageLabel));
        splitPane.setDividerLocation(350);

        // Layout the main components
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Display the image of the selected object
    private void displaySelectedImage() {
        int selectedIndex = objectList.getSelectedIndex();
        if (selectedIndex == -1)
            return;

        String selectedItem = objectList.getSelectedValue();
        try {
            // Extract ID from the selected item (format: "ID: Name")
            int id = Integer.parseInt(selectedItem.split(":")[0].trim());

            // Fetch the object from the database
            ObjetoPerdido obj = dbManager.searchObject(id);
            if (obj != null) {
                // Format the date in DD-MM-YYYY format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = obj.getFechaPerdida().format(formatter);

                // Display object info and image
                String infoText = "<html>Nombre: " + obj.getNombre() +
                        "<br>Descripción: " + obj.getDescripcion() +
                        "<br>Fecha: " + formattedDate + "</html>";

                if (obj.getRutaFoto() != null && !obj.getRutaFoto().isEmpty()) {
                    File imgFile = new File(obj.getRutaFoto());
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(obj.getRutaFoto());

                        // Scale the image if it's too large
                        if (icon.getIconWidth() > 350 || icon.getIconHeight() > 350) {
                            Image img = icon.getImage();
                            Image scaledImg = img.getScaledInstance(350, -1, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaledImg);
                        }

                        imageLabel.setIcon(icon);
                        imageLabel.setText(infoText);
                    } else {
                        imageLabel.setIcon(null);
                        imageLabel.setText(infoText + "<br>Imagen no encontrada: " + obj.getRutaFoto());
                    }
                } else {
                    imageLabel.setIcon(null);
                    imageLabel.setText(infoText + "<br>No hay imagen disponible");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageLabel.setIcon(null);
            imageLabel.setText("Error al cargar la imagen: " + e.getMessage());
        }
    }

    // Method to refresh the list of objects
    public void refreshObjectList() {
        listModel.clear();
        try {
            List<ObjetoPerdido> objects = dbManager.listObjects();
            for (ObjetoPerdido obj : objects) {
                listModel.addElement(obj.getId() + ": " + obj.getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar la lista de objetos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getter for the database manager
    public DatabaseManager getDbManager() {
        return dbManager;
    }
}
