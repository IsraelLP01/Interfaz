public class ObjetoPerdido {
    private int id;
    private String nombre;
    private String descripcion;
    private String fechaPerdida;
    private String rutaFoto;

    public ObjetoPerdido(int id, String nombre, String descripcion, String fechaPerdida, String rutaFoto) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaPerdida = fechaPerdida;
        this.rutaFoto = rutaFoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaPerdida() {
        return fechaPerdida;
    }

    public void setFechaPerdida(String fechaPerdida) {
        this.fechaPerdida = fechaPerdida;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
}