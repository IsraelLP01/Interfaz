import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);  // Gris oscuro
    private static final Color PANEL_COLOR = new Color(60, 60, 60);       // Gris un poco más claro
    private static final Color TEXT_COLOR = new Color(230, 230, 230);     // Texto casi blanco
    private static final Color BUTTON_BLUE = new Color(59, 89, 182);      // Azul para botones
    private static final Color BUTTON_ORANGE = new Color(255, 127, 0);    // Naranja para botón de eliminar
    private static final Color BORDER_COLOR = new Color(80, 80, 80);      // Borde gris

    private JList<ObjetoPerdido> objectList;
    private DefaultListModel<ObjetoPerdido> listModel;
    private DatabaseManager dbManager;
    private Map<Integer, ImageIcon> imageCache = new HashMap<>();
    private Rectangle buttonRect = new Rectangle();

    public MainFrame(DatabaseManager dbManager) {
        this.dbManager = dbManager;

        setTitle("Sistema de Objetos Perdidos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Establecer el color de fondo de la ventana principal
        getContentPane().setBackground(BACKGROUND_COLOR);

        initializeUI();
    }

    private void initializeUI() {
        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel lateral izquierdo para botones
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(PANEL_COLOR);
        sidePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        sidePanel.setPreferredSize(new Dimension(150, 500));
        
        // Crear botones principales
        JButton addButton = createStyledButton("Agregar Objeto", BUTTON_BLUE);
        JButton deleteButton = createStyledButton("Eliminar Objeto", BUTTON_ORANGE); // Naranja para este botón
        JButton searchButton = createStyledButton("Buscar Objeto", BUTTON_BLUE);
        
        // Aplicar máximo ancho a los botones
        Dimension buttonSize = new Dimension(130, 40);
        addButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        searchButton.setMaximumSize(buttonSize);

        // Configurar acciones de botones
        addButton.addActionListener(e -> {
            AddObjectDialog dialog = new AddObjectDialog(this);
            dialog.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            DeleteObjectDialog dialog = new DeleteObjectDialog(this);
            dialog.setVisible(true);
        });

        searchButton.addActionListener(e -> {
            SearchObjectDialog dialog = new SearchObjectDialog(this);
            dialog.setVisible(true);
        });
        
        // Añadir botones al panel lateral con espaciado
        sidePanel.add(addButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        sidePanel.add(deleteButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        sidePanel.add(searchButton);
        sidePanel.add(Box.createVerticalGlue());  // Empujar botones hacia arriba

        // Crear el modelo de lista y JList
        listModel = new DefaultListModel<>();
        objectList = new JList<>(listModel);
        objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectList.setCellRenderer(new DetailedObjectRenderer());
        objectList.setFixedCellHeight(120);
        objectList.setBackground(PANEL_COLOR);
        objectList.setForeground(TEXT_COLOR);
        objectList.setSelectionBackground(BUTTON_BLUE);
        objectList.setSelectionForeground(Color.WHITE);
        
        // Agregar detector de clics para el botón de ver imagen
        objectList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = objectList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Rectangle cellBounds = objectList.getCellBounds(index, index);
                    // Ajustar la posición de buttonRect relativa a la celda
                    if (buttonRect != null && 
                        e.getX() >= cellBounds.x + buttonRect.x && 
                        e.getX() <= cellBounds.x + buttonRect.x + buttonRect.width && 
                        e.getY() >= cellBounds.y + buttonRect.y && 
                        e.getY() <= cellBounds.y + buttonRect.y + buttonRect.height) {
                        
                        ObjetoPerdido obj = objectList.getModel().getElementAt(index);
                        if (obj.getRutaFoto() != null && !obj.getRutaFoto().isEmpty()) {
                            new ImageViewerDialog(MainFrame.this, obj.getRutaFoto(), obj.getNombre()).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(MainFrame.this, 
                                "Este objeto no tiene imagen asociada", 
                                "Sin imagen", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(objectList);
        listScrollPane.setBackground(PANEL_COLOR);
        listScrollPane.getViewport().setBackground(PANEL_COLOR);
        listScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        // Título para la lista
        JLabel titleLabel = new JLabel("Objetos Perdidos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Panel central con título y lista
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(listScrollPane, BorderLayout.CENTER);
        
        // Agregar componentes al panel principal
        mainPanel.add(sidePanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Agregar panel principal a la ventana
        getContentPane().add(mainPanel);
    }
    
    // Método para crear botones con estilo consistente
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
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

    // Clase para renderizar objetos en la lista con toda la información
    private class DetailedObjectRenderer extends JPanel implements ListCellRenderer<ObjetoPerdido> {
        private JLabel imageLabel;
        private JLabel idLabel;
        private JLabel nameLabel;
        private JLabel descLabel;
        private JLabel dateLabel;
        private JButton viewImageButton;
        private JPanel infoPanel;
        private JPanel westPanel;
        
        public DetailedObjectRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            
            // Panel para la imagen
            imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(100, 100));
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
            imageLabel.setForeground(TEXT_COLOR);
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(70, 70, 70)); // Fondo un poco más claro para la imagen
            
            // Panel para la información de texto
            infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(4, 1, 0, 2));
            infoPanel.setBackground(PANEL_COLOR);
            
            idLabel = new JLabel();
            idLabel.setFont(new Font("Arial", Font.BOLD, 12));
            idLabel.setForeground(TEXT_COLOR);
            
            nameLabel = new JLabel();
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabel.setForeground(TEXT_COLOR);
            
            descLabel = new JLabel();
            descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            descLabel.setForeground(TEXT_COLOR);
            
            dateLabel = new JLabel();
            dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            dateLabel.setForeground(new Color(200, 200, 200)); // Gris claro para la fecha
            
            infoPanel.add(idLabel);
            infoPanel.add(nameLabel);
            infoPanel.add(descLabel);
            infoPanel.add(dateLabel);
            
            // Botón para ver la imagen
            viewImageButton = new JButton("Ver Imagen");
            viewImageButton.setBackground(BUTTON_BLUE);
            viewImageButton.setForeground(Color.WHITE);
            viewImageButton.setFocusPainted(false);
            viewImageButton.setBorderPainted(false);
            viewImageButton.setPreferredSize(new Dimension(100, 30));
            
            westPanel = new JPanel(new BorderLayout(0, 5));
            westPanel.setBackground(PANEL_COLOR);
            westPanel.add(imageLabel, BorderLayout.CENTER);
            westPanel.add(viewImageButton, BorderLayout.SOUTH);
            
            add(westPanel, BorderLayout.WEST);
            add(infoPanel, BorderLayout.CENTER);
            
            setOpaque(true);
            setBackground(PANEL_COLOR);
        }
        
        @Override
        public Component getListCellRendererComponent(JList<? extends ObjetoPerdido> list, ObjetoPerdido obj, 
                                                    int index, boolean isSelected, boolean cellHasFocus) {
            // Configurar la información del objeto
            idLabel.setText("ID: " + obj.getId());
            nameLabel.setText("Nombre: " + obj.getNombre());
            descLabel.setText("Descripción: " + obj.getDescripcion());
            
            // Formatear la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = obj.getFechaPerdida().format(formatter);
            dateLabel.setText("Fecha: " + formattedDate);
            
            // Cargar imagen del objeto
            if (obj.getRutaFoto() != null && !obj.getRutaFoto().isEmpty()) {
                // Usar caché para mejorar rendimiento
                ImageIcon icon = imageCache.get(obj.getId());
                if (icon == null) {
                    File imgFile = new File(obj.getRutaFoto());
                    if (imgFile.exists()) {
                        icon = new ImageIcon(obj.getRutaFoto());
                        // Redimensionar la imagen para la miniatura
                        Image img = icon.getImage();
                        Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(resizedImg);
                        imageCache.put(obj.getId(), icon);
                    } else {
                        icon = null;
                    }
                }
                imageLabel.setIcon(icon);
                imageLabel.setText("");
                viewImageButton.setEnabled(true);
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Sin imagen");
                viewImageButton.setEnabled(false);
            }
            
            // Almacenar la posición del botón para detectar clics
            buttonRect.setBounds(viewImageButton.getBounds());
            
            // Estilos según selección
            if (isSelected) {
                setBackground(BUTTON_BLUE.darker());
                infoPanel.setBackground(BUTTON_BLUE.darker());
                westPanel.setBackground(BUTTON_BLUE.darker());
            } else {
                setBackground(PANEL_COLOR);
                infoPanel.setBackground(PANEL_COLOR);
                westPanel.setBackground(PANEL_COLOR);
            }
            
            return this;
        }
    }

    // Método para refrescar la lista de objetos
    public void refreshObjectList() {
        listModel.clear();
        imageCache.clear(); // Limpiar caché de imágenes
        try {
            List<ObjetoPerdido> objects = dbManager.listObjects();
            for (ObjetoPerdido obj : objects) {
                listModel.addElement(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar la lista de objetos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getter para el manejador de base de datos
    public DatabaseManager getDbManager() {
        return dbManager;
    }
}
