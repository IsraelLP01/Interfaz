import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteObjectDialog extends JDialog {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);  // Gris oscuro
    private static final Color PANEL_COLOR = new Color(60, 60, 60);       // Gris un poco más claro
    private static final Color TEXT_COLOR = new Color(230, 230, 230);     // Texto casi blanco
    private static final Color BUTTON_BLUE = new Color(59, 89, 182);      // Azul para botones
    private static final Color BUTTON_ORANGE = new Color(255, 127, 0);    // Naranja para botón de eliminar
    private static final Color BORDER_COLOR = new Color(80, 80, 80);      // Borde gris
    
    private JComboBox<String> objectComboBox;
    private JButton deleteButton;
    private JButton cancelButton;

    public DeleteObjectDialog(Frame parent) {
        super(parent, "Eliminar Objeto Perdido", true);
        setBackground(BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Configurar panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(PANEL_COLOR);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Panel para instrucciones
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(PANEL_COLOR);
        JLabel instructionLabel = new JLabel("Seleccione el objeto a eliminar:");
        instructionLabel.setForeground(TEXT_COLOR);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionPanel.add(instructionLabel);
        
        // Crear el combo box para seleccionar el objeto
        objectComboBox = new JComboBox<>();
        objectComboBox.setBackground(PANEL_COLOR);
        objectComboBox.setForeground(TEXT_COLOR);
        objectComboBox.setPreferredSize(new Dimension(250, 30));
        ((JComponent) objectComboBox.getRenderer()).setOpaque(true);
        
        // Crear el panel para el combobox
        JPanel comboPanel = new JPanel();
        comboPanel.setBackground(PANEL_COLOR);
        comboPanel.add(objectComboBox);
        
        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_COLOR);
        
        // Botón para eliminar el objeto (naranja)
        deleteButton = new JButton("Eliminar");
        styleButton(deleteButton, BUTTON_ORANGE);
        deleteButton.addActionListener(e -> deleteObject());
        
        // Botón para cancelar (azul)
        cancelButton = new JButton("Cancelar");
        styleButton(cancelButton, BUTTON_BLUE);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        
        // Agregar componentes al panel principal
        mainPanel.add(instructionPanel, BorderLayout.NORTH);
        mainPanel.add(comboPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Cargar objetos en el combobox
        loadObjects();
        
        // Configurar tamaño y posición
        setSize(350, 200);
        setLocationRelativeTo(parent);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 35));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
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
            // Confirmar eliminación con un diálogo
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este objeto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(selected.split(":")[0]);
                MainFrame parentFrame = (MainFrame) getParent();
                parentFrame.getDbManager().deleteObject(id);
                JOptionPane.showMessageDialog(this, "Objeto eliminado exitosamente.");
                parentFrame.refreshObjectList(); // Actualiza lista
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un objeto para eliminar.",
                "Selección inválida", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}