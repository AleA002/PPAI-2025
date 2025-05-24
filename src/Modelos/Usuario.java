package Modelos;

public class Usuario {
    private String nombreUsuario;
    private String contrasena;
    private Empleado empleado;


    public Usuario(String nombreUsuario, String contrasena, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.empleado = empleado;
    }


    public Empleado getEmpleado() {
        return empleado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", empleado=" + empleado +
                '}';
    }
}


