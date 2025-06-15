import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.text.JTextComponent;

public class AddObjectDialog extends JDialog {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);  // Gris oscuro
    private static final Color PANEL_COLOR = new Color(60, 60, 60);       // Gris un poco más claro
    private static final Color TEXT_COLOR = new Color(230, 230, 230);     // Texto casi blanco
    private static final Color BUTTON_BLUE = new Color(59, 89, 182);      // Azul para botones
    private static final Color BUTTON_ORANGE = new Color(255, 127, 0);    // Naranja para botón de eliminar
    private static final Color BORDER_COLOR = new Color(80, 80, 80);      // Borde gris
    
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
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create components
        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setForeground(TEXT_COLOR);
        nameField = new JTextField(20);
        configureTextField(nameField);

        JLabel descriptionLabel = new JLabel("Descripción:");
        descriptionLabel.setForeground(TEXT_COLOR);
        descriptionField = new JTextArea(5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        configureTextComponent(descriptionField);
        JScrollPane descScrollPane = new JScrollPane(descriptionField);
        descScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        descScrollPane.getViewport().setBackground(PANEL_COLOR);

        JLabel dateLabel = new JLabel("Fecha (DD-MM-YYYY):");
        dateLabel.setForeground(TEXT_COLOR);
        dateField = new JTextField(10);
        configureTextField(dateField);

        // Add "Hoy" button
        JButton todayButton = createStyledButton("Hoy", BUTTON_BLUE);
        todayButton.addActionListener(e -> {
            // Get current date and format it as DD-MM-YYYY
            String today = LocalDate.now().format(dateFormatter);
            dateField.setText(today);
        });

        photoButton = createStyledButton("Seleccionar Foto", BUTTON_BLUE);
        photoPathLabel = new JLabel("Sin foto seleccionada");
        photoPathLabel.setForeground(TEXT_COLOR);
        photoPreview = new JLabel();
        photoPreview.setPreferredSize(new Dimension(200, 200));
        photoPreview.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        photoPreview.setHorizontalAlignment(JLabel.CENTER);
        photoPreview.setOpaque(true);
        photoPreview.setBackground(new Color(70, 70, 70));
        photoPreview.setForeground(TEXT_COLOR);

        JButton addButton = createStyledButton("Agregar", BUTTON_BLUE);
        JButton cancelButton = createStyledButton("Cancelar", BUTTON_BLUE);

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
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_COLOR);
        formPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
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
        photoPathLabel.setPreferredSize(new Dimension(300, 20));
        formPanel.add(photoPathLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(photoPreview, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setSize(550, 600);
        setLocationRelativeTo(parent);
    }
    
    private void configureTextField(JTextField field) {
        field.setBackground(PANEL_COLOR);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }
    
    private void configureTextComponent(JTextComponent textComp) {
        textComp.setBackground(PANEL_COLOR);
        textComp.setForeground(TEXT_COLOR);
        textComp.setCaretColor(TEXT_COLOR);
        textComp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
}
