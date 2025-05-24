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
    public CambioEstado(Estado estado, Empleado responsable) {
        this.estado = estado;
        this.responsableInspeccion = responsable;
        this.fechaHoraInicio = LocalDateTime.now();
    }

    // metodos

}
