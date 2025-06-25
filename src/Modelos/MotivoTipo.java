package Modelos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MotivoTipo {
    // atributos
    private String descripcion;

    // constructor
    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    //getters
    public String getDescripcion() {
        return descripcion;
    }
    public static List<MotivoTipo> getMotivosTipo() {
        List<MotivoTipo> listaMotivos = new ArrayList<>();
        listaMotivos.add(new MotivoTipo("Sin combustible"));
        listaMotivos.add(new MotivoTipo("Mantenimiento programado"));
        listaMotivos.add(new MotivoTipo("Rotura de motor"));
        listaMotivos.add(new MotivoTipo("Se rompió"));
        return listaMotivos;
    }

    public static MotivoTipo parse(String nombre) {
        return new MotivoTipo(nombre);
    }

    @Override
    public String toString() {
        return "MotivoTipo{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}
