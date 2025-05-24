package Modelos;

public class Empleado {
    //atributos
    private String mail;
    private String apellido;
    private String nombre;
    private String telefono;
    private Rol rol;


    //constructor
    public Empleado(String mail, String apellido, String nombre, String telefono, Rol rol) {
        this.mail = mail;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
        this.rol = rol;
    }
    //getters
    public String getApellido() {
        return apellido;
    }
    public String getMail() {
        return mail;
    }
    public String getNombre() {
        return nombre;
    }
    public String getTelefono() {
        return telefono;
    }
    public Rol getRol() {
        return rol;
    }



    public boolean esResponsableReparacion() {
        return rol != null && "ResponsableReparacion".equalsIgnoreCase(rol.getNombreRol());
    }

    //to String
    @Override
    public String toString() {
        return "Empleado{" +
                "mail='" + mail + '\'' +
                ", apellido='" + apellido + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rol=" + rol +
                '}';
    }
}
