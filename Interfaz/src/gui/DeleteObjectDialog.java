import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteObjectDialog extends JDialog {
    private JComboBox<String> objectComboBox;
    private JButton deleteButton;
    private JButton cancelButton;

    public DeleteObjectDialog(Frame parent) {
        super(parent, "Eliminar Objeto Perdido", true);
        setLayout(new FlowLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);

        // Crear el combo box para seleccionar el objeto
        objectComboBox = new JComboBox<>();
        loadObjects(); // Método para cargar objetos desde la base de datos
        add(objectComboBox);

        // Botón para eliminar el objeto
        deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteObject();
            }
        });
        add(deleteButton);

        // Botón para cancelar
        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(cancelButton);
    }

    private void loadObjects() {
        // Aquí se cargarían los objetos perdidos desde la base de datos
        // Por ejemplo: objectComboBox.addItem("Objeto 1");
    }

    private void deleteObject() {
        String selectedObject = (String) objectComboBox.getSelectedItem();
        if (selectedObject != null) {
            // Aquí se implementaría la lógica para eliminar el objeto de la base de datos
            JOptionPane.showMessageDialog(this, "Objeto eliminado: " + selectedObject);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un objeto para eliminar.");
        }
    }
}