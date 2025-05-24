package Principal;

import Modelos.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        System.out.println("Ruta actual de ejecución: " + new java.io.File(".").getAbsolutePath());
        PantallaCerrarOrdenInspeccion pantalla = new PantallaCerrarOrdenInspeccion("src/ordenesInspeccion.json");
        GestorCerrarOrdenInspeccion gestor = new GestorCerrarOrdenInspeccion(pantalla, "src/ordenesInspeccion.json");

        // Crear y configurar la ventana principal
        JFrame frame = new JFrame("Ventana Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Crear un botón
        JButton boton = new JButton("Cerrar Orden De Inspeccion");

        Sesion sesion = gestor.getSesionActual();

        boton.addActionListener(e -> pantalla.tomarOpcionCerrarOrdenDeInspeccion());

        frame.getContentPane().add(boton);
        frame.setVisible(true);
    }
}