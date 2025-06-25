package Modelos;

import java.util.List;
import java.util.ArrayList;

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

    // metodos

    public static List<Empleado> getEmpleados() {
        List<Empleado> listaEmpleados = new ArrayList<>();

        Rol rolEmpleado = new Rol("Empleado", "Empleado Random"); // rol genérico
        Rol rolResponsableInscripcion = new Rol("Responsable Inscripcion", "Es el responsable de Inscribir");
        Rol rolResponsableReparacion = new Rol("Responsable Reparacion", "Es el responsable de Reparar");

        listaEmpleados.add(new Empleado("mgonzalez@example.com", "González", "María", "351-1234567", rolResponsableReparacion));
        listaEmpleados.add(new Empleado("jrodriguez@example.com", "Rodríguez", "Juan", "11-7654321", rolResponsableReparacion));
        listaEmpleados.add(new Empleado("cfernandez@example.com", "Fernández", "Carla", "261-3344556", rolEmpleado));
        listaEmpleados.add(new Empleado("tomi@gmail.com", "Gomez", "Tomas", "3518319242", rolResponsableInscripcion));
        listaEmpleados.add(new Empleado("leomessi@gmail.com", "Messi", "Leo", "118319242", rolResponsableInscripcion));

        return listaEmpleados;
    }

    public boolean esResponsableReparacion() {
        if ((this.getRol().getNombreRol()).equals("Responsable Reparacion")) {
            return true;
        }
        return false;
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
