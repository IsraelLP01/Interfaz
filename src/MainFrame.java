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
<<<<<<< HEAD
    private static final Color BUTTON_RED = new Color(220, 53, 69);    // Rojo para botón de eliminar
    private static final Color BUTTON_ORANGE = new Color(255, 140, 0);    // Naranja para botón de cambiar estatus
=======
    private static final Color BUTTON_ORANGE = new Color(255, 127, 0);    // Naranja para botón de eliminar
>>>>>>> origin/main
    private static final Color BORDER_COLOR = new Color(80, 80, 80);      // Borde gris

    private JList<ObjetoPerdido> objectList;
    private DefaultListModel<ObjetoPerdido> listModel;
    private DatabaseManager dbManager;
    private Map<Integer, ImageIcon> imageCache = new HashMap<>();
<<<<<<< HEAD
=======
    private Rectangle buttonRect = new Rectangle();
>>>>>>> origin/main

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
<<<<<<< HEAD
        JButton deleteButton = createStyledButton("Eliminar Objeto", BUTTON_RED); // Cambiado de BUTTON_ORANGE a BUTTON_RED
        JButton searchButton = createStyledButton("Buscar Objeto", BUTTON_BLUE);
        JButton editButton = createStyledButton("Editar Objeto", BUTTON_BLUE);
        JButton viewImageButton = createStyledButton("Ver Imagen", BUTTON_BLUE);     // Nuevo botón para ver imagen
        JButton changeStatusButton = createStyledButton("Cambiar Estatus", BUTTON_ORANGE); // Nuevo botón para cambiar estatus
=======
        JButton deleteButton = createStyledButton("Eliminar Objeto", BUTTON_ORANGE); // Naranja para este botón
        JButton searchButton = createStyledButton("Buscar Objeto", BUTTON_BLUE);
>>>>>>> origin/main
        
        // Aplicar máximo ancho a los botones
        Dimension buttonSize = new Dimension(130, 40);
        addButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        searchButton.setMaximumSize(buttonSize);
<<<<<<< HEAD
        editButton.setMaximumSize(buttonSize);
        viewImageButton.setMaximumSize(buttonSize);      // Tamaño para botón de ver imagen
        changeStatusButton.setMaximumSize(buttonSize);   // Tamaño para botón de cambiar estatus
=======
>>>>>>> origin/main

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
        
<<<<<<< HEAD
        // Configurar acción para el botón de editar
        editButton.addActionListener(e -> {
            // Verificar si hay un objeto seleccionado
            ObjetoPerdido selected = objectList.getSelectedValue();
            if (selected != null) {
                EditObjectDialog dialog = new EditObjectDialog(this, selected);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Por favor seleccione un objeto para editar.",
                    "Selección requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Configurar acción para el botón de ver imagen
        viewImageButton.addActionListener(e -> {
            ObjetoPerdido selected = objectList.getSelectedValue();
            if (selected != null) {
                if (selected.getRutaFoto() != null && !selected.getRutaFoto().isEmpty()) {
                    new ImageViewerDialog(this, selected.getRutaFoto(), selected.getNombre()).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Este objeto no tiene imagen asociada", 
                        "Sin imagen", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Por favor seleccione un objeto para ver su imagen.",
                    "Selección requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Configurar acción para el botón de cambiar estatus
        changeStatusButton.addActionListener(e -> {
            ObjetoPerdido selected = objectList.getSelectedValue();
            if (selected != null) {
                cambiarEstatus(selected);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Por favor seleccione un objeto para cambiar su estatus.",
                    "Selección requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Crear un panel para los botones que NO requieren selección
        JPanel noSelectionPanel = new JPanel();
        noSelectionPanel.setLayout(new BoxLayout(noSelectionPanel, BoxLayout.Y_AXIS));
        noSelectionPanel.setBackground(PANEL_COLOR);
        noSelectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            "Acciones",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            TEXT_COLOR
        ));

        // Añadir botones que NO requieren selección
        noSelectionPanel.add(addButton);
        noSelectionPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        noSelectionPanel.add(searchButton);

        // Crear un panel para los botones que SÍ requieren selección
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBackground(PANEL_COLOR);
        selectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            "Acciones Objeto",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            TEXT_COLOR
        ));

        // Añadir botones que SÍ requieren selección
        selectionPanel.add(viewImageButton);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        selectionPanel.add(editButton);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        selectionPanel.add(changeStatusButton);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        selectionPanel.add(deleteButton);

        // Añadir ambos grupos al panel lateral con espacio entre ellos
        sidePanel.add(noSelectionPanel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 25)));
        sidePanel.add(selectionPanel);
        sidePanel.add(Box.createVerticalGlue());  // Empujar todo hacia arriba
=======
        // Añadir botones al panel lateral con espaciado
        sidePanel.add(addButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        sidePanel.add(deleteButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        sidePanel.add(searchButton);
        sidePanel.add(Box.createVerticalGlue());  // Empujar botones hacia arriba
>>>>>>> origin/main

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
        
<<<<<<< HEAD
        // Añadir doble clic para ver la imagen
        objectList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    ObjetoPerdido selected = objectList.getSelectedValue();
                    if (selected != null && selected.getRutaFoto() != null && !selected.getRutaFoto().isEmpty()) {
                        new ImageViewerDialog(MainFrame.this, selected.getRutaFoto(), selected.getNombre()).setVisible(true);
=======
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
>>>>>>> origin/main
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
<<<<<<< HEAD
        private JLabel statusLabel; // Etiqueta para estatus
=======
        private JButton viewImageButton;
>>>>>>> origin/main
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
<<<<<<< HEAD
            infoPanel.setLayout(new GridLayout(5, 1, 0, 2)); // 5 filas para incluir estatus
=======
            infoPanel.setLayout(new GridLayout(4, 1, 0, 2));
>>>>>>> origin/main
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
            
<<<<<<< HEAD
            // Etiqueta para el estatus
            statusLabel = new JLabel();
            statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
            statusLabel.setForeground(new Color(250, 250, 100)); // Color llamativo para el estatus
            
=======
>>>>>>> origin/main
            infoPanel.add(idLabel);
            infoPanel.add(nameLabel);
            infoPanel.add(descLabel);
            infoPanel.add(dateLabel);
<<<<<<< HEAD
            infoPanel.add(statusLabel); // Añadir etiqueta de estatus
            
            westPanel = new JPanel(new BorderLayout());
            westPanel.setBackground(PANEL_COLOR);
            westPanel.add(imageLabel, BorderLayout.CENTER);
=======
            
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
>>>>>>> origin/main
            
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
            
<<<<<<< HEAD
            // Formatear la fecha perdida
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDateLost = obj.getFechaPerdida().format(formatter);
            dateLabel.setText("Fecha de pérdida: " + formattedDateLost);
            
            // Mostrar información de estatus y fecha encontrado
            String statusText = "Estatus: " + obj.getEstatus();
            if (obj.getEstatus().equals("Encontrado") && obj.getFechaEncontrado() != null) {
                statusText += " (" + obj.getFechaEncontrado().format(formatter) + ")";
            }
            statusLabel.setText(statusText);
            
            // Configurar color según el estatus
            if ("Encontrado".equals(obj.getEstatus())) {
                statusLabel.setForeground(new Color(100, 250, 100)); // Verde para encontrado
            } else {
                statusLabel.setForeground(new Color(250, 100, 100)); // Rojo para perdido
            }
=======
            // Formatear la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = obj.getFechaPerdida().format(formatter);
            dateLabel.setText("Fecha: " + formattedDate);
>>>>>>> origin/main
            
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
<<<<<<< HEAD
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Sin imagen");
            }
            
=======
                viewImageButton.setEnabled(true);
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Sin imagen");
                viewImageButton.setEnabled(false);
            }
            
            // Almacenar la posición del botón para detectar clics
            buttonRect.setBounds(viewImageButton.getBounds());
            
>>>>>>> origin/main
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

<<<<<<< HEAD
    // Método para cambiar el estatus de un objeto
    private void cambiarEstatus(ObjetoPerdido obj) {
        String nuevoEstatus = "Perdido".equals(obj.getEstatus()) ? "Encontrado" : "Perdido";
        
        // Confirmar el cambio
        int option = JOptionPane.showConfirmDialog(this,
            "¿Desea cambiar el estatus del objeto \"" + obj.getNombre() + 
            "\" a \"" + nuevoEstatus + "\"?",
            "Cambiar Estatus", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            dbManager.updateObjectStatus(obj.getId(), nuevoEstatus);
            JOptionPane.showMessageDialog(this, 
                "Estatus cambiado a \"" + nuevoEstatus + "\"." +
                (nuevoEstatus.equals("Encontrado") ? "\nSe ha registrado la fecha actual." : ""),
                "Estatus Actualizado", JOptionPane.INFORMATION_MESSAGE);
            refreshObjectList();
        }
    }

    // Este método debe añadirse a la clase MainFrame
    /**
     * Selecciona un objeto en la lista por su ID
     * @param id El ID del objeto a seleccionar
     */
    public void selectObject(int id) {
        // Buscar el objeto en el modelo de lista
        for (int i = 0; i < listModel.size(); i++) {
            ObjetoPerdido obj = listModel.getElementAt(i);
            if (obj.getId() == id) {
                // Seleccionar el objeto
                objectList.setSelectedIndex(i);
                // Asegurarse de que el objeto es visible en la lista
                objectList.ensureIndexIsVisible(i);
                return;
            }
        }
        
        // Si no se encontró, mostrar mensaje
        JOptionPane.showMessageDialog(this, 
            "No se pudo encontrar el objeto con ID " + id + " en la lista.",
            "Objeto no encontrado", JOptionPane.WARNING_MESSAGE);
    }

=======
>>>>>>> origin/main
    // Getter para el manejador de base de datos
    public DatabaseManager getDbManager() {
        return dbManager;
    }
}
