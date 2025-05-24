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
    //metodos
    public String getNombreRol() {
        return nombreRol;
    }

}
