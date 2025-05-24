package Modelos;

import java.time.LocalDateTime;

public class Sesion {
    private Usuario usuario;
    private LocalDateTime fechaInicio;
    private boolean activa;

    public Sesion(Usuario usuario, LocalDateTime fechaInicio, boolean activa) {
        this.usuario = usuario;
        this.fechaInicio = fechaInicio;
        this.activa = false;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "Sesion{" +
                "usuario=" + usuario +
                ", fechaInicio=" + fechaInicio +
                ", activa=" + activa +
                '}';
    }
}


