package Modelos;

import java.time.LocalDateTime;

public class Sismografo {
    // atributos
    private int idSismografo;
    private LocalDateTime fechaAdquisicion;
    private String nroSerie;
    private EstacionSismologica estacionSismologica;


    // constructor
    public Sismografo(int id, LocalDateTime fechaAdq, String nroSerie, EstacionSismologica est) {
        this.idSismografo = id;
        this.fechaAdquisicion = fechaAdq;
        this.nroSerie = nroSerie;
        this.estacionSismologica = est;
    }

}
