import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchObjectDialog extends JDialog {
    private JTextField searchField;
    private JTextArea resultArea;
    private JButton searchButton;

    public SearchObjectDialog(Frame parent) {
        super(parent, "Buscar Objeto Perdido", true);
        setLayout(new BorderLayout());

        // Panel para los criterios de búsqueda
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        searchButton = new JButton("Buscar");

        searchPanel.add(new JLabel("Ingrese el nombre del objeto:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Área de resultados
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Acción del botón de búsqueda
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchObject();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    private void searchObject() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        resultArea.setText("Resultados de búsqueda para: " + searchTerm + "\n");

        MainFrame parentFrame = (MainFrame) getParent();
        for (ObjetoPerdido obj : parentFrame.getDbManager().listObjects()) {
            if (obj.getNombre().toLowerCase().contains(searchTerm)) {
                resultArea.append("ID: " + obj.getId() + "\nNombre: " + obj.getNombre() +
                        "\nDescripción: " + obj.getDescripcion() +
                        "\nFecha: " + obj.getFechaPerdida() + "\n\n");
            }
        }
    }
}