import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.text.JTextComponent;

public class EditObjectDialog extends JDialog {
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
    private JComboBox<String> statusComboBox;
    private JTextField dateFoundField;
    private final String[] photoPath = { "" };
    private ObjetoPerdido objetoOriginal;

    // Date formatter for DD-MM-YYYY format
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public EditObjectDialog(Frame parent, ObjetoPerdido objeto) {
        super(parent, "Editar Objeto: " + objeto.getNombre(), true);
        getContentPane().setBackground(BACKGROUND_COLOR);
        this.objetoOriginal = objeto;
        
        // Guardar la ruta de la foto original
        if (objeto.getRutaFoto() != null && !objeto.getRutaFoto().isEmpty()) {
            photoPath[0] = objeto.getRutaFoto();
        }

        // Create components
        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setForeground(TEXT_COLOR);
        nameField = new JTextField(objeto.getNombre(), 20);
        configureTextField(nameField);

        JLabel descriptionLabel = new JLabel("Descripción:");
        descriptionLabel.setForeground(TEXT_COLOR);
        descriptionField = new JTextArea(objeto.getDescripcion(), 5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        configureTextComponent(descriptionField);
        JScrollPane descScrollPane = new JScrollPane(descriptionField);
        descScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        descScrollPane.getViewport().setBackground(PANEL_COLOR);

        JLabel dateLabel = new JLabel("Fecha pérdida (DD-MM-YYYY):");
        dateLabel.setForeground(TEXT_COLOR);
        dateField = new JTextField(objeto.getFechaPerdida().format(dateFormatter), 10);
        configureTextField(dateField);

        // Add "Hoy" button
        JButton todayButton = createStyledButton("Hoy", BUTTON_BLUE);
        todayButton.addActionListener(e -> {
            // Get current date and format it as DD-MM-YYYY
            String today = LocalDate.now().format(dateFormatter);
            dateField.setText(today);
        });
        
        // Selector de estatus
        JLabel statusLabel = new JLabel("Estatus:");
        statusLabel.setForeground(TEXT_COLOR);
        statusComboBox = new JComboBox<>(new String[]{"Perdido", "Encontrado"});
        statusComboBox.setSelectedItem(objeto.getEstatus());
        statusComboBox.setBackground(PANEL_COLOR);
        statusComboBox.setForeground(TEXT_COLOR);
        ((JComponent)statusComboBox.getRenderer()).setOpaque(true);
        
        // Campo para fecha encontrado
        JLabel dateFoundLabel = new JLabel("Fecha encontrado (DD-MM-YYYY):");
        dateFoundLabel.setForeground(TEXT_COLOR);
        dateFoundField = new JTextField(10);
        configureTextField(dateFoundField);
        if (objeto.getFechaEncontrado() != null) {
            dateFoundField.setText(objeto.getFechaEncontrado().format(dateFormatter));
        }
        
        // Habilitar/deshabilitar campo fecha encontrado según el estatus
        statusComboBox.addActionListener(e -> {
            boolean isFound = "Encontrado".equals(statusComboBox.getSelectedItem());
            dateFoundField.setEnabled(isFound);
            if (isFound && dateFoundField.getText().trim().isEmpty()) {
                dateFoundField.setText(LocalDate.now().format(dateFormatter));
            }
        });
        
        // "Hoy" button para fecha encontrado
        JButton todayFoundButton = createStyledButton("Hoy", BUTTON_BLUE);
        todayFoundButton.addActionListener(e -> {
            String today = LocalDate.now().format(dateFormatter);
            dateFoundField.setText(today);
        });
        
        // Inicializar campo de fecha encontrado
        dateFoundField.setEnabled("Encontrado".equals(statusComboBox.getSelectedItem()));

        photoButton = createStyledButton("Cambiar Foto", BUTTON_BLUE);
        photoPathLabel = new JLabel(photoPath[0].isEmpty() ? "Sin foto seleccionada" : photoPath[0]);
        photoPathLabel.setForeground(TEXT_COLOR);
        photoPreview = new JLabel();
        photoPreview.setPreferredSize(new Dimension(200, 200));
        photoPreview.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        photoPreview.setHorizontalAlignment(JLabel.CENTER);
        photoPreview.setOpaque(true);
        photoPreview.setBackground(new Color(70, 70, 70));
        photoPreview.setForeground(TEXT_COLOR);
        
        // Cargar foto existente si hay
        if (!photoPath[0].isEmpty()) {
            ImageIcon icon = new ImageIcon(photoPath[0]);
            if (icon.getIconWidth() > 200 || icon.getIconHeight() > 200) {
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
            }
            photoPreview.setIcon(icon);
        } else {
            photoPreview.setText("Sin imagen");
        }

        JButton saveButton = createStyledButton("Guardar", BUTTON_BLUE);
        JButton cancelButton = createStyledButton("Cancelar", BUTTON_BLUE);

        // Set up photo selection
        photoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imágenes", "jpg", "jpeg", "png", "gif"));

            int result = fileChooser.showOpenDialog(EditObjectDialog.this);
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

        // Set up save button action
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String date = dateField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();
            String dateFound = dateFoundField.getText().trim();
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
                        "El formato de fecha de pérdida debe ser DD-MM-YYYY (ej: 14-06-2025)",
                        "Formato de fecha inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if ("Encontrado".equals(status) && !dateFound.isEmpty() && !Validator.isValidDate(dateFound)) {
                JOptionPane.showMessageDialog(this,
                        "El formato de fecha de encontrado debe ser DD-MM-YYYY (ej: 14-06-2025)",
                        "Formato de fecha inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Convert date strings to LocalDate
                LocalDate localDateLost = LocalDate.parse(date, dateFormatter);
                LocalDate localDateFound = null;
                if ("Encontrado".equals(status) && !dateFound.isEmpty()) {
                    localDateFound = LocalDate.parse(dateFound, dateFormatter);
                }

                // Get database manager
                MainFrame mainFrame = (MainFrame) getParent();
                DatabaseManager dbManager = mainFrame.getDbManager();

                // Actualizar el objeto
                ObjetoPerdido objetoActualizado = new ObjetoPerdido(
                    objetoOriginal.getId(), name, description, localDateLost, photo, status, localDateFound
                );
                
                // Update object in database
                dbManager.updateObject(objetoActualizado);

                // Refresh list and close dialog
                mainFrame.refreshObjectList();
                JOptionPane.showMessageDialog(this,
                        "Objeto actualizado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al actualizar el objeto: " + ex.getMessage(),
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
        
        // Status field
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(statusComboBox, gbc);
        
        // Date found field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(dateFoundLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dateFoundField, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(todayFoundButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(photoButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        photoPathLabel.setPreferredSize(new Dimension(300, 20));
        formPanel.add(photoPathLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(photoPreview, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setSize(550, 650);
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