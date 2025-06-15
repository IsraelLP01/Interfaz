import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageViewerDialog extends JDialog {
    
    private ZoomableImagePanel imagePanel;
    private JSlider zoomSlider;
    private JLabel zoomPercentLabel;
    private JComboBox<String> zoomPresets;
    
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);  // Gris oscuro
    private static final Color PANEL_COLOR = new Color(60, 60, 60);       // Gris un poco más claro
    private static final Color TEXT_COLOR = new Color(230, 230, 230);     // Texto casi blanco
    private static final Color BUTTON_BLUE = new Color(59, 89, 182);      // Azul para botones
    private static final Color BUTTON_ORANGE = new Color(255, 127, 0);    // Naranja para botón de eliminar
    private static final Color BORDER_COLOR = new Color(80, 80, 80);      // Borde gris
    private static final Color HEADER_COLOR = new Color(45, 45, 45);      // Color para la barra superior
    private static final Color FOOTER_COLOR = new Color(40, 40, 40);      // Color para la barra inferior
    
    public ImageViewerDialog(JFrame parent, String imagePath, String objectName) {
        super(parent, "Visor de Imagen: " + objectName, true);
        
        try {
            // Cargar la imagen desde el archivo
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            
            // Configurar el layout principal
            setLayout(new BorderLayout());
            getContentPane().setBackground(BACKGROUND_COLOR);
            
            // Crear el panel con la imagen
            imagePanel = new ZoomableImagePanel(originalImage);
            
            // 1. Crear barra de herramientas superior (ribbon estilo Word)
            JPanel ribbonPanel = createRibbonPanel();
            add(ribbonPanel, BorderLayout.NORTH);
            
            // 2. Crear el panel central para la imagen
            JScrollPane scrollPane = new JScrollPane(imagePanel);
            scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            scrollPane.getViewport().setBackground(new Color(35, 35, 35)); // Fondo muy oscuro para el scrollpane
            add(scrollPane, BorderLayout.CENTER);
            
            // 3. Crear barra de estado inferior
            JPanel statusBar = createStatusBar();
            add(statusBar, BorderLayout.SOUTH);
            
            // Configurar el tamaño y posición
            setSize(900, 650);
            setLocationRelativeTo(parent);
            
            // Permitir cerrar con Escape
            getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
            );
            
            // Ajustar imagen al tamaño inicial de la ventana
            SwingUtilities.invokeLater(() -> {
                imagePanel.fitToViewport();
                updateZoomControls();
            });
            
            // Escuchar cambios de tamaño de la ventana
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (imagePanel.isAutoFit()) {
                        imagePanel.fitToViewport();
                        updateZoomControls();
                    }
                }
            });
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Error al cargar la imagen: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    
    private JPanel createRibbonPanel() {
        JPanel ribbonPanel = new JPanel();
        ribbonPanel.setLayout(new BorderLayout());
        ribbonPanel.setBackground(HEADER_COLOR);
        ribbonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Panel de herramientas con FlowLayout
        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolsPanel.setBackground(HEADER_COLOR);
        
        // Botón de ajustar a ventana
        JButton fitToWindowButton = createToolbarButton("Ajustar", "Ajustar imagen a la ventana");
        fitToWindowButton.addActionListener(e -> {
            imagePanel.resetZoom();
            updateZoomControls();
        });
        
        // Botón para cerrar
        JButton closeButton = createToolbarButton("Cerrar", "Cerrar el visor de imágenes");
        closeButton.addActionListener(e -> dispose());
        
        // Selector de zoom preestablecido
        String[] zoomOptions = {"10%", "25%", "50%", "75%", "100%", "150%", "200%", "300%", "400%", "Ajustar a ventana"};
        zoomPresets = new JComboBox<>(zoomOptions);
        zoomPresets.setSelectedItem("100%");
        zoomPresets.setMaximumSize(new Dimension(120, 28));
        zoomPresets.setPreferredSize(new Dimension(120, 28));
        zoomPresets.setBackground(PANEL_COLOR);
        zoomPresets.setForeground(TEXT_COLOR);
        ((JComponent)zoomPresets.getRenderer()).setOpaque(true);
        
        zoomPresets.addActionListener(e -> {
            String selected = (String)zoomPresets.getSelectedItem();
            if ("Ajustar a ventana".equals(selected)) {
                imagePanel.resetZoom();
            } else {
                try {
                    // Extraer el valor numérico quitando el signo %
                    String percentStr = selected.replace("%", "").trim();
                    double zoomPercent = Double.parseDouble(percentStr);
                    imagePanel.setZoom(zoomPercent / 100.0);
                } catch (NumberFormatException ex) {
                    // En caso de error, no hacer nada
                }
            }
            updateZoomControls();
        });
        
        // Label de zoom
        JLabel zoomLabel = new JLabel("Zoom:");
        zoomLabel.setForeground(TEXT_COLOR);
        
        // Agregar componentes a la barra de herramientas
        toolsPanel.add(fitToWindowButton);
        toolsPanel.add(new JSeparator(SwingConstants.VERTICAL) {
            {
                setPreferredSize(new Dimension(1, 28));
                setForeground(BORDER_COLOR);
            }
        });
        toolsPanel.add(zoomLabel);
        toolsPanel.add(zoomPresets);
        toolsPanel.add(new JSeparator(SwingConstants.VERTICAL) {
            {
                setPreferredSize(new Dimension(1, 28));
                setForeground(BORDER_COLOR);
            }
        });
        toolsPanel.add(closeButton);
        
        ribbonPanel.add(toolsPanel, BorderLayout.CENTER);
        
        return ribbonPanel;
    }
    
    private JButton createToolbarButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        button.setBackground(BUTTON_BLUE);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(BUTTON_BLUE.brighter());
            }
            
            public void mouseExited(MouseEvent evt) {
                button.setBackground(BUTTON_BLUE);
            }
        });
        
        return button;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.setBackground(FOOTER_COLOR);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        
        // Panel izquierdo para información
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        infoPanel.setBackground(FOOTER_COLOR);
        
        // Información de la imagen
        JLabel imageInfoLabel = new JLabel();
        imageInfoLabel.setText("Dimensiones: " + imagePanel.getImageWidth() + " x " + imagePanel.getImageHeight() + " px");
        imageInfoLabel.setForeground(TEXT_COLOR);
        infoPanel.add(imageInfoLabel);
        
        // Panel derecho para controles de zoom
        JPanel zoomControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        zoomControlPanel.setBackground(FOOTER_COLOR);
        
        // Botón de zoom menos
        JButton zoomOutButton = new JButton("-");
        zoomOutButton.setFocusPainted(false);
        zoomOutButton.setFont(new Font("Arial", Font.BOLD, 14));
        zoomOutButton.setBackground(BUTTON_BLUE);
        zoomOutButton.setForeground(Color.WHITE);
        zoomOutButton.setBorderPainted(false);
        
        // Efecto hover
        zoomOutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                zoomOutButton.setBackground(BUTTON_BLUE.brighter());
            }
            
            public void mouseExited(MouseEvent evt) {
                zoomOutButton.setBackground(BUTTON_BLUE);
            }
        });
        
        zoomOutButton.addActionListener(e -> {
            imagePanel.zoomOut();
            updateZoomControls();
        });
        
        // Slider de zoom
        zoomSlider = new JSlider(JSlider.HORIZONTAL, 10, 400, 100);
        zoomSlider.setPreferredSize(new Dimension(120, 20));
        zoomSlider.setBackground(FOOTER_COLOR);
        zoomSlider.setForeground(TEXT_COLOR);
        
        // Label para el porcentaje
        zoomPercentLabel = new JLabel("100%");
        zoomPercentLabel.setPreferredSize(new Dimension(50, 20));
        zoomPercentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        zoomPercentLabel.setForeground(TEXT_COLOR);
        
        // Botón de zoom más
        JButton zoomInButton = new JButton("+");
        zoomInButton.setFocusPainted(false);
        zoomInButton.setFont(new Font("Arial", Font.BOLD, 14));
        zoomInButton.setBackground(BUTTON_BLUE);
        zoomInButton.setForeground(Color.WHITE);
        zoomInButton.setBorderPainted(false);
        
        // Efecto hover
        zoomInButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                zoomInButton.setBackground(BUTTON_BLUE.brighter());
            }
            
            public void mouseExited(MouseEvent evt) {
                zoomInButton.setBackground(BUTTON_BLUE);
            }
        });
        
        zoomInButton.addActionListener(e -> {
            imagePanel.zoomIn();
            updateZoomControls();
        });
        
        // Agregar listener al slider
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = zoomSlider.getValue();
                zoomPercentLabel.setText(value + "%");
                
                // Solo aplicar zoom cuando el usuario suelta el slider
                if (!zoomSlider.getValueIsAdjusting()) {
                    imagePanel.setZoom(value / 100.0);
                }
            }
        });
        
        // Agregar componentes al panel de control de zoom
        zoomControlPanel.add(zoomOutButton);
        zoomControlPanel.add(zoomSlider);
        zoomControlPanel.add(zoomPercentLabel);
        zoomControlPanel.add(zoomInButton);
        
        // Agregar paneles a la barra de estado
        statusBar.add(infoPanel, BorderLayout.WEST);
        statusBar.add(zoomControlPanel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    private void updateZoomControls() {
        int zoomPercent = (int)(imagePanel.getZoomFactor() * 100);
        zoomSlider.setValue(zoomPercent);
        zoomPercentLabel.setText(zoomPercent + "%");
        
        // Actualizar el combobox
        String percentStr = zoomPercent + "%";
        
        boolean presetExists = false;
        for (int i = 0; i < zoomPresets.getItemCount(); i++) {
            if (zoomPresets.getItemAt(i).equals(percentStr)) {
                zoomPresets.setSelectedIndex(i);
                presetExists = true;
                break;
            }
        }
        
        if (!presetExists && !imagePanel.isAutoFit()) {
            zoomPresets.setSelectedItem(percentStr);
        } else if (imagePanel.isAutoFit()) {
            zoomPresets.setSelectedItem("Ajustar a ventana");
        }
    }
    
    // Panel para mostrar la imagen con zoom
    private class ZoomableImagePanel extends JPanel {
        private BufferedImage image;
        private double zoomFactor = 1.0;
        private Point dragStartPoint;
        private Point viewPosition = new Point(0, 0);
        private boolean autoFit = true; // Controla si la imagen se ajusta automáticamente
        
        public ZoomableImagePanel(BufferedImage image) {
            this.image = image;
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            setBackground(new Color(35, 35, 35)); // Fondo muy oscuro para la imagen
            
            // Agregar soporte para arrastrar la imagen
            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dragStartPoint = e.getPoint();
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStartPoint != null && zoomFactor > getAutoFitZoom()) {
                        Point currentPoint = e.getPoint();
                        int dx = currentPoint.x - dragStartPoint.x;
                        int dy = currentPoint.y - dragStartPoint.y;
                        
                        viewPosition.x -= dx;
                        viewPosition.y -= dy;
                        
                        // Limitar el área de panorámica
                        limitViewPosition();
                        
                        dragStartPoint = currentPoint;
                        
                        // Cuando el usuario arrastra, desactivamos el ajuste automático
                        autoFit = false;
                        repaint();
                    }
                }
                
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    // Zoom con rueda del ratón
                    double oldZoom = zoomFactor;
                    
                    if (e.getWheelRotation() < 0) {
                        zoomFactor *= 1.2;
                    } else {
                        if (zoomFactor > 0.1) zoomFactor /= 1.2;
                    }
                    
                    // Si el zoom cambió, actualizar y sincronizar el slider
                    if (oldZoom != zoomFactor) {
                        updateSize();
                        autoFit = false;
                        // Actualizar controles de zoom
                        updateZoomControls();
                    }
                }
            };
            
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
            addMouseWheelListener(mouseHandler);
        }
        
        public int getImageWidth() {
            return image.getWidth();
        }
        
        public int getImageHeight() {
            return image.getHeight();
        }
        
        // Calcula el factor de zoom para ajustar la imagen al viewport
        private double getAutoFitZoom() {
            if (getParent() == null) return 1.0;
            
            JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
            if (viewport == null) return 1.0;
            
            double widthRatio = (double)viewport.getWidth() / image.getWidth();
            double heightRatio = (double)viewport.getHeight() / image.getHeight();
            
            // Usamos el valor menor para que la imagen completa sea visible
            return Math.min(widthRatio, heightRatio) * 0.95; // 95% para dar un poco de margen
        }
        
        // Ajusta la imagen al tamaño del viewport
        public void fitToViewport() {
            JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
            if (viewport != null) {
                zoomFactor = getAutoFitZoom();
                viewPosition.x = 0;
                viewPosition.y = 0;
                updateSize();
                autoFit = true;
            }
        }
        
        public boolean isAutoFit() {
            return autoFit;
        }
        
        public double getZoomFactor() {
            return zoomFactor;
        }
        
        // Establecer un nivel de zoom específico
        public void setZoom(double newZoomFactor) {
            if (newZoomFactor < 0.1) newZoomFactor = 0.1;
            if (newZoomFactor > 10) newZoomFactor = 10;
            
            zoomFactor = newZoomFactor;
            autoFit = false;
            updateSize();
        }
        
        private void limitViewPosition() {
            int maxX = (int)(image.getWidth() * zoomFactor) - getWidth();
            int maxY = (int)(image.getHeight() * zoomFactor) - getHeight();
            
            if (maxX < 0) {
                viewPosition.x = 0;
            } else {
                viewPosition.x = Math.max(0, Math.min(viewPosition.x, maxX));
            }
            
            if (maxY < 0) {
                viewPosition.y = 0;
            } else {
                viewPosition.y = Math.max(0, Math.min(viewPosition.y, maxY));
            }
        }
        
        public void zoomIn() {
            zoomFactor *= 1.2;
            updateSize();
            autoFit = false;
        }
        
        public void zoomOut() {
            if (zoomFactor > 0.1) {
                zoomFactor /= 1.2;
                updateSize();
                autoFit = false;
            }
        }
        
        public void resetZoom() {
            // En lugar de volver a 1.0, ajustamos al tamaño del viewport
            fitToViewport();
        }
        
        private void updateSize() {
            limitViewPosition();
            
            int width = (int)(image.getWidth() * zoomFactor);
            int height = (int)(image.getHeight() * zoomFactor);
            setPreferredSize(new Dimension(width, height));
            
            revalidate();
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Dibujar fondo oscuro
            g.setColor(new Color(35, 35, 35));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                               RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
            
            int w = getWidth();
            int h = getHeight();
            
            // Ajustar la transformación para aplicar zoom y panorámica
            AffineTransform at = new AffineTransform();
            at.scale(zoomFactor, zoomFactor);
            
            // Calcular la posición para centrar la imagen cuando es más pequeña que el panel
            int x = Math.max(0, (w - (int)(image.getWidth() * zoomFactor)) / 2);
            int y = Math.max(0, (h - (int)(image.getHeight() * zoomFactor)) / 2);
            
            // Aplicar panorámica solo si la imagen es más grande que el panel
            if (image.getWidth() * zoomFactor > w) {
                x = -viewPosition.x;
            }
            if (image.getHeight() * zoomFactor > h) {
                y = -viewPosition.y;
            }
            
            // Dibujar sombra ligera debajo de la imagen 
            int shadowSize = 5;
            int shadowOffset = 2;
            if (zoomFactor >= getAutoFitZoom()) {
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRect(x + shadowOffset, y + shadowOffset, 
                           (int)(image.getWidth() * zoomFactor) + shadowSize, 
                           (int)(image.getHeight() * zoomFactor) + shadowSize);
            }
            
            g2d.translate(x, y);
            g2d.transform(at);
            g2d.drawImage(image, 0, 0, null);
            
            // Si la imagen tiene un zoom menor al 100%, dibujar un borde
            if (zoomFactor < 1.0) {
                g2d.setColor(BORDER_COLOR);
                g2d.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
            }
            
            g2d.dispose();
        }
    }
}