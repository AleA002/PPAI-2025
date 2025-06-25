package Modelos;

public class Rol {
    //atributos
    private String nombreRol;
    private String descripcionRol;

    // constructor
    public Rol(String nombreRol, String descripcionRol) {
        this.nombreRol = nombreRol;
        this.descripcionRol = descripcionRol;
    }

    //getters
    public String getNombreRol() {
        return nombreRol;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    //setters
    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }


    @Override
    public String toString() {
        return "Rol{" +
                "nombreRol='" + nombreRol + '\'' +
                ", descripcionRol='" + descripcionRol + '\'' +
                '}';
    }
}
