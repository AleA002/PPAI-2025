package Modelos;

import java.time.LocalDateTime;

public class CambioEstado {
    // atributos
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Estado estado;
    private MotivoFueraDeServicio motivoFueraDeServicio;
    private Empleado responsableInspeccion;

    // constructor
    public CambioEstado(Estado estado, Empleado responsableInspeccion, LocalDateTime fechaHoraInicio, MotivoFueraDeServicio motivoFueraDeServicio) {
        this.estado = estado;
        this.responsableInspeccion = responsableInspeccion;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = null;
        this.motivoFueraDeServicio = motivoFueraDeServicio;
    }

    public CambioEstado(Estado estado, Empleado responsableInspeccion, LocalDateTime fechaHoraInicio) {
        this.estado = estado;
        this.responsableInspeccion = responsableInspeccion;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = null;
    }

    //getters

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public Estado getEstado() {
        return estado;
    }

    public MotivoFueraDeServicio getMotivoFueraDeServicio() {
        return motivoFueraDeServicio;
    }

    public Empleado getResponsableInspeccion() {
        return responsableInspeccion;
    }


    // setters

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setMotivoFueraDeServicio(MotivoFueraDeServicio motivoFueraDeServicio) {
        this.motivoFueraDeServicio = motivoFueraDeServicio;
    }

    public void setResponsableInspeccion(Empleado responsableInspeccion) {
        this.responsableInspeccion = responsableInspeccion;
    }


    // metodos

    public Boolean sosActual() {
        if (this.getFechaHoraFin() == null) {
            return true;
        }
        return false;
    }

    public CambioEstado crearCambioEstado(LocalDateTime fechaHoraInicio, Empleado responsable, MotivoFueraDeServicio motivoFueraDeServicio) { //metodo del Sismografo
        // System.out.println("CAMBIO DE ESTADO CREADO");
        return new CambioEstado(new Estado("Fuera de Servicio", "Sismografo"), responsable, fechaHoraInicio, motivoFueraDeServicio);
    }


    @Override
    public String toString() {
        return "CambioEstado{" +
                "fechaHoraInicio=" + fechaHoraInicio +
                ", fechaHoraFin=" + fechaHoraFin +
                ", estado=" + estado +
                ", motivoFueraDeServicio=" + motivoFueraDeServicio +
                ", responsableInspeccion=" + responsableInspeccion +
                '}';
    }
}
