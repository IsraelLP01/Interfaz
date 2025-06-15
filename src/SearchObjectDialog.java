import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;

public class SearchObjectDialog extends JDialog {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);  // Gris oscuro
    private static final Color PANEL_COLOR = new Color(60, 60, 60);       // Gris un poco más claro
    private static final Color TEXT_COLOR = new Color(230, 230, 230);     // Texto casi blanco
    private static final Color BUTTON_BLUE = new Color(59, 89, 182);      // Azul para botones
    private static final Color BUTTON_ORANGE = new Color(255, 127, 0);    // Naranja para botón de eliminar
    private static final Color BORDER_COLOR = new Color(80, 80, 80);      // Borde gris
    
    private JTextField searchField;
    private JTextArea resultArea;
    private JButton searchButton;

    public SearchObjectDialog(Frame parent) {
        super(parent, "Buscar Objeto Perdido", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel para los criterios de búsqueda
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(PANEL_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Label de búsqueda
        JLabel searchLabel = new JLabel("Ingrese el nombre del objeto:");
        searchLabel.setForeground(TEXT_COLOR);
        
        // Campo de búsqueda
        searchField = new JTextField(20);
        searchField.setBackground(new Color(70, 70, 70));
        searchField.setForeground(TEXT_COLOR);
        searchField.setCaretColor(TEXT_COLOR);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        
        // Botón de búsqueda
        searchButton = new JButton("Buscar");
        searchButton.setBackground(BUTTON_BLUE);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        
        // Efecto hover para el botón
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(BUTTON_BLUE.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(BUTTON_BLUE);
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Área de resultados
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(70, 70, 70));
        resultArea.setForeground(TEXT_COLOR);
        resultArea.setCaretColor(TEXT_COLOR);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(new Color(70, 70, 70));
        
        // Panel para el área de resultados con título
        JPanel resultPanel = new JPanel(new BorderLayout(5, 5));
        resultPanel.setBackground(PANEL_COLOR);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel resultTitle = new JLabel("Resultados de búsqueda:");
        resultTitle.setForeground(TEXT_COLOR);
        resultTitle.setFont(new Font("Arial", Font.BOLD, 14));
        resultPanel.add(resultTitle, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón para cerrar
        JButton closeButton = new JButton("Cerrar");
        closeButton.setBackground(BUTTON_BLUE);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        // Efecto hover para el botón cerrar
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(BUTTON_BLUE.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(BUTTON_BLUE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        
        // Acción del botón de búsqueda
        searchButton.addActionListener(e -> searchObject());
        
        // También buscar al presionar Enter en el campo de búsqueda
        searchField.addActionListener(e -> searchObject());
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(parent);
    }

    private void searchObject() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            resultArea.setText("Por favor ingrese un término de búsqueda.");
            return;
        }
        
        resultArea.setText("Resultados de búsqueda para: \"" + searchTerm + "\"\n");
        resultArea.append("----------------------------------------\n\n");
        
        boolean found = false;
        MainFrame parentFrame = (MainFrame) getParent();
        for (ObjetoPerdido obj : parentFrame.getDbManager().listObjects()) {
            if (obj.getNombre().toLowerCase().contains(searchTerm) || 
                obj.getDescripcion().toLowerCase().contains(searchTerm)) {
                
                found = true;
                resultArea.append("ID: " + obj.getId() + "\n");
                resultArea.append("Nombre: " + obj.getNombre() + "\n");
                resultArea.append("Descripción: " + obj.getDescripcion() + "\n");
                resultArea.append("Fecha: " + obj.getFechaPerdida().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\n");
                resultArea.append("Foto: " + (obj.getRutaFoto() != null && !obj.getRutaFoto().isEmpty() ? 
                    "Sí" : "No") + "\n");
                resultArea.append("\n----------------------------------------\n\n");
            }
        }
        
        if (!found) {
            resultArea.append("No se encontraron objetos que coincidan con el término de búsqueda.\n");
        }
        
        // Volver al inicio del área de texto
        resultArea.setCaretPosition(0);
    }
}