import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
<<<<<<< HEAD
import java.io.File;
=======
>>>>>>> origin/main

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
    private JScrollPane scrollPane;  // Guardar referencia al JScrollPane
    private JPanel resultsContainerPanel; // Panel para contener resultados individuales
    
    // Referencia al objeto padre MainFrame
    private MainFrame parentFrame;

    public SearchObjectDialog(Frame parent) {
        super(parent, "Buscar Objeto Perdido", true);
        this.parentFrame = (MainFrame) parent;
        
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

<<<<<<< HEAD
        // Panel de resultados con scroll
        resultsContainerPanel = new JPanel();
        resultsContainerPanel.setBackground(new Color(70, 70, 70));
        resultsContainerPanel.setLayout(new BoxLayout(resultsContainerPanel, BoxLayout.Y_AXIS));
        
        scrollPane = new JScrollPane(resultsContainerPanel);
=======
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
>>>>>>> origin/main
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
        setSize(600, 500);
    }

    private void searchObject() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
<<<<<<< HEAD
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un término de búsqueda.",
                    "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Limpiar resultados anteriores
        resultsContainerPanel.removeAll();
        
        // Añadir título de resultados
        JLabel titleLabel = new JLabel("Resultados de búsqueda para: \"" + searchTerm + "\"");
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        resultsContainerPanel.add(titleLabel);
        
        boolean found = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
=======
            resultArea.setText("Por favor ingrese un término de búsqueda.");
            return;
        }
        
        resultArea.setText("Resultados de búsqueda para: \"" + searchTerm + "\"\n");
        resultArea.append("----------------------------------------\n\n");
        
        boolean found = false;
        MainFrame parentFrame = (MainFrame) getParent();
>>>>>>> origin/main
        for (ObjetoPerdido obj : parentFrame.getDbManager().listObjects()) {
            if (obj.getNombre().toLowerCase().contains(searchTerm) || 
                obj.getDescripcion().toLowerCase().contains(searchTerm)) {
                
                found = true;
<<<<<<< HEAD
                
                // Panel para este resultado
                JPanel resultPanel = new JPanel();
                resultPanel.setLayout(new BorderLayout());
                resultPanel.setBackground(new Color(80, 80, 80));
                resultPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                resultPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
                resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                // Información del objeto
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(new Color(80, 80, 80));
                
                JLabel idLabel = new JLabel("ID: " + obj.getId());
                idLabel.setForeground(TEXT_COLOR);
                idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel nameLabel = new JLabel("Nombre: " + obj.getNombre());
                nameLabel.setForeground(TEXT_COLOR);
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel descLabel = new JLabel("Descripción: " + obj.getDescripcion());
                descLabel.setForeground(TEXT_COLOR);
                descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel dateLabel = new JLabel("Fecha pérdida: " + obj.getFechaPerdida().format(formatter));
                dateLabel.setForeground(TEXT_COLOR);
                dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel statusLabel = new JLabel("Estatus: " + obj.getEstatus());
                statusLabel.setForeground("Encontrado".equals(obj.getEstatus()) ? 
                        new Color(100, 250, 100) : new Color(250, 100, 100));
                statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                infoPanel.add(idLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(nameLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(descLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(dateLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(statusLabel);
                
                if (obj.getEstatus().equals("Encontrado") && obj.getFechaEncontrado() != null) {
                    JLabel dateFoundLabel = new JLabel("Fecha encontrado: " + obj.getFechaEncontrado().format(formatter));
                    dateFoundLabel.setForeground(TEXT_COLOR);
                    dateFoundLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    infoPanel.add(dateFoundLabel);
                }
                
                // Panel para los botones
                JPanel actionPanel = new JPanel();
                actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                actionPanel.setBackground(new Color(80, 80, 80));
                
                // Botón para ir al objeto en la lista
                JButton selectButton = new JButton("Seleccionar en la lista");
                selectButton.setBackground(BUTTON_BLUE);
                selectButton.setForeground(Color.WHITE);
                selectButton.setFocusPainted(false);
                selectButton.setBorderPainted(false);
                
                // Crear una referencia final al objeto para usar en el listener
                final ObjetoPerdido foundObject = obj;
                
                selectButton.addActionListener(e -> {
                    // Cerrar el diálogo
                    dispose();
                    
                    // Seleccionar el objeto en la lista principal
                    parentFrame.selectObject(foundObject.getId());
                });
                
                // Efecto hover para el botón
                selectButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        selectButton.setBackground(BUTTON_BLUE.brighter());
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        selectButton.setBackground(BUTTON_BLUE);
                    }
                });
                
                actionPanel.add(selectButton);
                
                resultPanel.add(infoPanel, BorderLayout.CENTER);
                resultPanel.add(actionPanel, BorderLayout.SOUTH);
                
                resultsContainerPanel.add(resultPanel);
                resultsContainerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
=======
                resultArea.append("ID: " + obj.getId() + "\n");
                resultArea.append("Nombre: " + obj.getNombre() + "\n");
                resultArea.append("Descripción: " + obj.getDescripcion() + "\n");
                resultArea.append("Fecha: " + obj.getFechaPerdida().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\n");
                resultArea.append("Foto: " + (obj.getRutaFoto() != null && !obj.getRutaFoto().isEmpty() ? 
                    "Sí" : "No") + "\n");
                resultArea.append("\n----------------------------------------\n\n");
>>>>>>> origin/main
            }
        }
        
        if (!found) {
<<<<<<< HEAD
            JLabel noResultsLabel = new JLabel("No se encontraron objetos que coincidan con el término de búsqueda.");
            noResultsLabel.setForeground(TEXT_COLOR);
            noResultsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            noResultsLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            resultsContainerPanel.add(noResultsLabel);
        }
        
        // Actualizar UI
        resultsContainerPanel.revalidate();
        resultsContainerPanel.repaint();
        
        // Asegurar que se muestra el inicio de los resultados
        scrollPane.getVerticalScrollBar().setValue(0);
=======
            resultArea.append("No se encontraron objetos que coincidan con el término de búsqueda.\n");
        }
        
        // Volver al inicio del área de texto
        resultArea.setCaretPosition(0);
>>>>>>> origin/main
    }
}