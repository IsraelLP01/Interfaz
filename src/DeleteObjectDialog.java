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
        MainFrame parentFrame = (MainFrame) getParent();
        DatabaseManager dbManager = parentFrame.getDbManager();
        for (ObjetoPerdido obj : dbManager.listObjects()) {
            objectComboBox.addItem(obj.getId() + ": " + obj.getNombre());
        }
    }

    private void deleteObject() {
        String selected = (String) objectComboBox.getSelectedItem();
        if (selected != null && selected.contains(":")) {
            int id = Integer.parseInt(selected.split(":")[0]);
            MainFrame parentFrame = (MainFrame) getParent();
            parentFrame.getDbManager().deleteObject(id);
            JOptionPane.showMessageDialog(this, "Objeto eliminado.");
            parentFrame.refreshObjectList(); // Actualiza lista
            dispose();
        }
    }
}