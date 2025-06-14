import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddObjectDialog extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionField;
    private JTextField dateField;
    private JLabel photoPreview;
    private JButton photoButton;
    private JLabel photoPathLabel;
    private final String[] photoPath = { "" };

    // Date formatter for DD-MM-YYYY format
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AddObjectDialog(Frame parent) {
        super(parent, "Agregar Objeto Perdido", true);

        // Create components
        JLabel nameLabel = new JLabel("Nombre:");
        nameField = new JTextField(20);

        JLabel descriptionLabel = new JLabel("Descripción:");
        descriptionField = new JTextArea(5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionField);

        JLabel dateLabel = new JLabel("Fecha (DD-MM-YYYY):");
        dateField = new JTextField(10);

        // Add "Hoy" button
        JButton todayButton = new JButton("Hoy");
        todayButton.addActionListener(e -> {
            // Get current date and format it as DD-MM-YYYY
            String today = LocalDate.now().format(dateFormatter);
            dateField.setText(today);
        });

        photoButton = new JButton("Seleccionar Foto");
        photoPathLabel = new JLabel("Sin foto seleccionada");
        photoPreview = new JLabel();
        photoPreview.setPreferredSize(new Dimension(200, 200));
        photoPreview.setBorder(BorderFactory.createEtchedBorder());
        photoPreview.setHorizontalAlignment(JLabel.CENTER);

        JButton addButton = new JButton("Agregar");
        JButton cancelButton = new JButton("Cancelar");

        // Set up photo selection
        photoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imágenes", "jpg", "jpeg", "png", "gif"));

            int result = fileChooser.showOpenDialog(AddObjectDialog.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                photoPath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                photoPathLabel.setText(photoPath[0]);

                // Show preview of selected image
                ImageIcon icon = new ImageIcon(photoPath[0]);
                if (icon.getIconWidth() > 200 || icon.getIconHeight() > 200) {
                    Image img = icon.getImage();
                    Image scaledImg = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImg);
                }
                photoPreview.setIcon(icon);
            }
        });

        // Set up add button action
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String date = dateField.getText().trim();
            String photo = photoPath[0];

            // Validate input
            if (name.isEmpty() || description.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor complete todos los campos obligatorios.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!Validator.isValidDate(date)) {
                JOptionPane.showMessageDialog(this,
                        "El formato de fecha debe ser DD-MM-YYYY (ej: 14-06-2025)",
                        "Formato de fecha inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Convert String to LocalDate using the new format
                LocalDate localDate = LocalDate.parse(date, dateFormatter);

                // Get database manager and add object
                MainFrame mainFrame = (MainFrame) getParent();
                DatabaseManager dbManager = mainFrame.getDbManager();

                // Create object and add to database
                ObjetoPerdido objeto = new ObjetoPerdido(0, name, description, localDate, photo);
                dbManager.addObject(objeto);

                // Refresh list and close dialog
                mainFrame.refreshObjectList();
                JOptionPane.showMessageDialog(this,
                        "Objeto agregado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al agregar el objeto: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Set up cancel button action
        cancelButton.addActionListener(e -> dispose());

        // Layout components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2; // Span two columns
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1; // Reset to 1 column
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2; // Span two columns
        formPanel.add(descScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1; // Reset to 1 column
        formPanel.add(dateLabel, gbc);

        // Date field + Today button in the same row
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        formPanel.add(dateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(todayButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(photoButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(photoPathLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(photoPreview, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setSize(500, 600);
        setLocationRelativeTo(parent);
    }
}
