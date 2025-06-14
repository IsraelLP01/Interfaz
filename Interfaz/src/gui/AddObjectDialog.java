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

    public AddObjectDialog(Frame parent) {
        super(parent, "Agregar Objeto Perdido", true);
        setLayout(new GridLayout(4, 2));

        // Crear campos de entrada
        JLabel nameLabel = new JLabel("Nombre:");
        nameField = new JTextField();
        JLabel descriptionLabel = new JLabel("Descripción:");
        descriptionField = new JTextField();
        JLabel dateLabel = new JLabel("Fecha de Pérdida:");
        dateField = new JTextField();

        // Crear botones
        addButton = new JButton("Agregar");
        cancelButton = new JButton("Cancelar");

        // Agregar componentes al diálogo
        add(nameLabel);
        add(nameField);
        add(descriptionLabel);
        add(descriptionField);
        add(dateLabel);
        add(dateField);
        add(addButton);
        add(cancelButton);

        // Configurar el tamaño y la visibilidad
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Manejar eventos de los botones
        JButton photoButton = new JButton("Seleccionar Foto");
        JLabel photoPathLabel = new JLabel("Sin foto seleccionada");
        final String[] photoPath = {""};

        photoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(AddObjectDialog.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                photoPath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                photoPathLabel.setText(photoPath[0]);
            }
            }
        });

        // Agregar los componentes de la foto al diálogo
        add(photoButton);
        add(photoPathLabel);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            // Lógica para agregar el objeto perdido
            String name = nameField.getText();
            String description = descriptionField.getText();
            String date = dateField.getText();
            String photo = photoPath[0];
            // Aquí se llamaría al método para agregar el objeto a la base de datos
            // DatabaseManager.addObject(new ObjetoPerdido(name, description, date, photo));
            dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}