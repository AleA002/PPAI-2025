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


    //setters
    public void setNroOrden(int nroOrden) {
        this.nroOrden = nroOrden;
    }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
    public void setFechaHoraFinalizacion(LocalDateTime fechaHoraFinalizacion) {
        this.fechaHoraFinalizacion = fechaHoraFinalizacion;
    }
    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }
    public void setObservacionCierre(String observacionCierre) {
        this.observacionCierre = observacionCierre;
    }
    public void setMotivoFueraDeServicio(MotivoFueraDeServicio motivoFueraDeServicio) {
        this.motivoFueraDeServicio = motivoFueraDeServicio;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
    }
    public void setEstacionSismologica(EstacionSismologica estacionSismologica) {
        this.estacionSismologica = estacionSismologica;
    }
    public void setHistorialEstados(List<CambioEstado> historialEstados) {
        this.historialEstados = historialEstados;
    }
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }



    //metodos
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