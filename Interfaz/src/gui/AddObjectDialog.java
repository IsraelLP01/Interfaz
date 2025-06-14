import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddObjectDialog extends JDialog {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField dateField;
    private JButton addButton;
    private JButton cancelButton;
    private JButton photoButton;
    private JLabel photoPathLabel;
    private String photoPath = "";

    public AddObjectDialog(Frame parent) {
        super(parent, "Agregar Objeto Perdido", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST; // Alinea a la izquierda
        gbc.fill = GridBagConstraints.NONE;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        add(nameField, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Descripción:"), gbc);
        descriptionField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        add(descriptionField, gbc);

        // Fecha de pérdida
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Fecha de Pérdida (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        add(dateField, gbc);

        // Foto
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Foto:"), gbc);
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoButton = new JButton("Seleccionar Foto");
        photoPathLabel = new JLabel("Sin foto seleccionada");
        photoPanel.add(photoButton, BorderLayout.WEST);
        photoPanel.add(photoPathLabel, BorderLayout.CENTER);
        gbc.gridx = 1; gbc.gridy = 3;
        add(photoPanel, gbc);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Agregar");
        cancelButton = new JButton("Cancelar");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        setSize(500, 350);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Acción para seleccionar foto
        photoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(AddObjectDialog.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    photoPath = fileChooser.getSelectedFile().getAbsolutePath();
                    photoPathLabel.setText(photoPath);
                }
            }
        });

        // Acción para agregar objeto
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                String date = dateField.getText().trim();
                String photo = photoPath;
                // Validación simple
                if (name.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(AddObjectDialog.this, "Nombre y fecha son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Aquí se llamaría al método para agregar el objeto a la base de datos
                // DatabaseManager.addObject(new ObjetoPerdido(name, description, date, photo));
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }
}