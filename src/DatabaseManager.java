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
        String sql = "INSERT INTO objetos_perdidos (nombre, descripcion, fecha_perdida, ruta_foto, estatus, fecha_encontrado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, objeto.getNombre());
            pstmt.setString(2, objeto.getDescripcion());
            pstmt.setDate(3, java.sql.Date.valueOf(objeto.getFechaPerdida()));
            pstmt.setString(4, objeto.getRutaFoto());
            pstmt.setString(5, objeto.getEstatus());
            
            // Si no hay fecha de encontrado, usar null
            LocalDate fechaEncontrado = objeto.getFechaEncontrado();
            if (fechaEncontrado != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(fechaEncontrado));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            
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
                LocalDate fechaEncontrado = null;
                if (rs.getDate("fecha_encontrado") != null) {
                    fechaEncontrado = rs.getDate("fecha_encontrado").toLocalDate();
                }
                
                return new ObjetoPerdido(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_perdida").toLocalDate(),
                        rs.getString("ruta_foto"),
                        rs.getString("estatus"),
                        fechaEncontrado);
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
                LocalDate fechaEncontrado = null;
                if (rs.getDate("fecha_encontrado") != null) {
                    fechaEncontrado = rs.getDate("fecha_encontrado").toLocalDate();
                }
                
                objetos.add(new ObjetoPerdido(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha_perdida").toLocalDate(),
                        rs.getString("ruta_foto"),
                        rs.getString("estatus"),
                        fechaEncontrado));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objetos;
    }

    public void updateObjectStatus(int id, String newStatus) {
        String sql = "UPDATE objetos_perdidos SET estatus = ?, fecha_encontrado = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            
            if ("Encontrado".equals(newStatus)) {
                // Si el estatus es "Encontrado", establecer la fecha actual
                pstmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            } else {
                // Si no, establecer fecha_encontrado como NULL
                pstmt.setNull(2, java.sql.Types.DATE);
            }
            
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateObject(ObjetoPerdido objeto) {
        String sql = "UPDATE objetos_perdidos SET nombre = ?, descripcion = ?, fecha_perdida = ?, " +
                    "ruta_foto = ?, estatus = ?, fecha_encontrado = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, objeto.getNombre());
            pstmt.setString(2, objeto.getDescripcion());
            pstmt.setDate(3, java.sql.Date.valueOf(objeto.getFechaPerdida()));
            pstmt.setString(4, objeto.getRutaFoto());
            pstmt.setString(5, objeto.getEstatus());
            
            LocalDate fechaEncontrado = objeto.getFechaEncontrado();
            if (fechaEncontrado != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(fechaEncontrado));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            
            pstmt.setInt(7, objeto.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

// Clase modelo actualizada
class ObjetoPerdido {
    private int id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaPerdida;
    private String rutaFoto;
    private String estatus;
    private LocalDate fechaEncontrado;

    public ObjetoPerdido(int id, String nombre, String descripcion, LocalDate fechaPerdida, String rutaFoto) {
        this(id, nombre, descripcion, fechaPerdida, rutaFoto, "Perdido", null);
    }

    public ObjetoPerdido(int id, String nombre, String descripcion, LocalDate fechaPerdida, String rutaFoto,
                        String estatus, LocalDate fechaEncontrado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaPerdida = fechaPerdida;
        this.rutaFoto = rutaFoto;
        this.estatus = estatus != null ? estatus : "Perdido"; // Por defecto es "Perdido"
        this.fechaEncontrado = fechaEncontrado;
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
    
    public String getEstatus() {
        return estatus;
    }
    
    public LocalDate getFechaEncontrado() {
        return fechaEncontrado;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public void setFechaPerdida(LocalDate fechaPerdida) {
        this.fechaPerdida = fechaPerdida;
    }
    
    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
    
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    
    public void setFechaEncontrado(LocalDate fechaEncontrado) {
        this.fechaEncontrado = fechaEncontrado;
    }
}
