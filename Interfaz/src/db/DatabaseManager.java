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

    public DatabaseManager(String dbUrl, String user, String password) {
        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addObject(ObjetoPerdido objeto) {
        String sql = "INSERT INTO objetos_perdidos (nombre, descripcion, fecha_perdida) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, objeto.getNombre());
            pstmt.setString(2, objeto.getDescripcion());
            pstmt.setDate(3, Date.valueOf(objeto.getFechaPerdida()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
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
                return new ObjetoPerdido(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"), rs.getDate("fecha_perdida").toLocalDate());
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
                objetos.add(new ObjetoPerdido(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"), rs.getDate("fecha_perdida").toLocalDate()));
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Assuming your ObjetoPerdido class starts like this
class ObjetoPerdido {
    private int id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaPerdida;

    public ObjetoPerdido(int id, String nombre, String descripcion, LocalDate fechaPerdida) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaPerdida = fechaPerdida;
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
}