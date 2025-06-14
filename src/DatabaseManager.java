import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class DatabaseManager {
    private Connection connection;

    // Constructor con conexión fija a XAMPP
    public DatabaseManager() {
        String dbUrl = "jdbc:mysql://localhost:3306/objetosdb";
        String user = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            System.out.println("Conexión establecida con la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }

    // Update the constructor to accept parameters
    public DatabaseManager(String dbUrl, String user, String password) {
        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            System.out.println("Conexión establecida con la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }
    }

    public void addObject(ObjetoPerdido objeto) {
        String sql = "INSERT INTO objetos_perdidos (nombre, descripcion, fecha_perdida, ruta_foto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, objeto.getNombre());
            pstmt.setString(2, objeto.getDescripcion());
            pstmt.setDate(3, java.sql.Date.valueOf(objeto.getFechaPerdida()));
            pstmt.setString(4, objeto.getRutaFoto());
            pstmt.executeUpdate();
        } catch (

        SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteObject(int id) {
        String sql = "DELETE FROM objetos_perdidos WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObjetoPerdido searchObject(int id) {
        String sql = "SELECT * FROM objetos_perdidos WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ObjetoPerdido(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_perdida").toLocalDate(),
                        rs.getString("ruta_foto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ObjetoPerdido> listObjects() {
        List<ObjetoPerdido> objetos = new ArrayList<>();
        String sql = "SELECT * FROM objetos_perdidos";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                objetos.add(new ObjetoPerdido(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_perdida").toLocalDate(),
                        rs.getString("ruta_foto")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objetos;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Clase modelo
class ObjetoPerdido {
    private int id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaPerdida;
    private String rutaFoto;

    public ObjetoPerdido(int id, String nombre, String descripcion, LocalDate fechaPerdida, String rutaFoto) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaPerdida = fechaPerdida;
        this.rutaFoto = rutaFoto;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaPerdida() {
        return fechaPerdida;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }
}
