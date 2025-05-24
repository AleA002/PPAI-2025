package Modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdenInspeccion {
    // atributos
    private int nroOrden;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraCierre;
    private String observacionCierre;
    private MotivoFueraDeServicio motivoFueraDeServicio;
    private Estado estado;
    private Sismografo sismografo;
    private EstacionSismologica estacionSismologica;
    private List<CambioEstado> historialEstados = new ArrayList<>();
    private Empleado empleado;

    public OrdenInspeccion() { }

    // constructor
    public OrdenInspeccion(int nroOrden, Sismografo sismografo, Empleado empleado) {
        this.nroOrden = nroOrden;
        this.sismografo = sismografo;
        this.empleado = empleado;
    }

    // getters
    public Estado getEstado() {
        return estado;
    }
    public LocalDateTime getFechaHoraFinalizacion() {
        return fechaHoraFinalizacion;
    }

    public int getNroOrden() {
        return nroOrden;
    }
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }
    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }
    public String getObservacionCierre() {
        return observacionCierre;
    }
    public MotivoFueraDeServicio getMotivoFueraDeServicio() {
        return motivoFueraDeServicio;
    }
    public Sismografo getSismografo() {
        return sismografo;
    }

    public EstacionSismologica getEstacionSismologica() {
        return estacionSismologica;
    }

    public List<CambioEstado> getHistorialEstados() {
        return historialEstados;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public boolean esResponsable(Empleado empleadoComparar) {
        if (empleadoComparar.getApellido().equals(empleado.getApellido())) {
            return true;
        }
        return false;
    }



    //to String
    @Override
    public String toString() {
        return "OrdenInspeccion{" +
                "nroOrden=" + nroOrden +
                ", fechaHoraFinalizacion=" + fechaHoraFinalizacion +
                ", fechaHoraCierre=" + fechaHoraCierre +
                ", observacionCierre='" + observacionCierre + '\'' +
                ", motivoFueraDeServicio=" + motivoFueraDeServicio +
                ", estado=" + estado +
                ", sismografo=" + sismografo +
                ", estacionSismologica=" + estacionSismologica +
                ", historialEstados=" + historialEstados +
                ", empleado=" + empleado +
                '}';
    }
}