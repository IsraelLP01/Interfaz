import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JList<String> objectList;
    private DefaultListModel<String> listModel;
    private JLabel imageLabel;

    public MainFrame() {
        setTitle("Bodega de Objetos Perdidos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Inicializa la interfaz
        initializeUI();

        // Agrega un JLabel para mostrar la imagen del objeto seleccionado
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));
    }

    private void initializeUI() {
        listModel = new DefaultListModel<>();
        objectList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(objectList);

        JButton addButton = new JButton("Agregar Objeto");
        JButton deleteButton = new JButton("Eliminar Objeto");
        JButton searchButton = new JButton("Buscar Objeto");
        JButton showAllButton = new JButton("Mostrar Todos");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddObjectDialog dialog = new AddObjectDialog(MainFrame.this);
                dialog.setVisible(true);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteObjectDialog dialog = new DeleteObjectDialog(MainFrame.this);
                dialog.setVisible(true);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchObjectDialog dialog = new SearchObjectDialog(MainFrame.this);
                dialog.setVisible(true);
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí se llamaría a un método para mostrar todos los objetos
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(showAllButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public void addObject(String object) {
        listModel.addElement(object);
    }

    public void removeObject(String object) {
        listModel.removeElement(object);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}