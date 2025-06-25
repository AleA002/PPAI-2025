package Modelos;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InterfazMonitorCCRS {

    public static void notificarNovedadesImportadas(Sismografo sismografoSeleccionado) {
        Timer timer = new Timer(3000, e -> {
            JFrame frame = new JFrame("Ventana Monitor");
            frame.setSize(1200, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // Panel principal con BorderLayout
            JPanel panel = new JPanel(new BorderLayout());

            // Leyenda arriba
            JLabel leyenda = new JLabel("La notificación sobre el cierre de la Orden de Inspección fue enviada con éxito al MONITOR.", JLabel.CENTER);
            leyenda.setFont(new Font("Arial", Font.ITALIC, 24));
            panel.add(leyenda, BorderLayout.NORTH);

            // Datos del sismógrafo en tabla
            String[] columnas = {"Campo", "Valor"};
            Object[][] datosTabla = obtenerDatosSismografo(sismografoSeleccionado);

            JTable tabla = new JTable(datosTabla, columnas);
            tabla.setEnabled(false);
            tabla.setFont(new Font("Arial", Font.PLAIN, 12));
            tabla.setRowHeight(20);
            JScrollPane scrollPane = new JScrollPane(tabla);

            panel.add(scrollPane, BorderLayout.CENTER);

            // Mostrar ventana
            frame.add(panel);
            frame.setVisible(true);
        });

        timer.setRepeats(false);
        timer.start();
    }

    // Método auxiliar para extraer los campos y valores del sismógrafo con reflexión
    private static Object[][] obtenerDatosSismografo(Sismografo sismografo) {
        try {
            java.util.List<Object[]> datosList = new ArrayList<>();

            // Obtener solo el campo "id"
            Field campoId = sismografo.getClass().getDeclaredField("idSismografo");
            campoId.setAccessible(true);
            Object valorId = campoId.get(sismografo);
            datosList.add(new Object[]{"idSismografo", valorId});

            // Obtener el campo actualCambioEstado
            Field campoCambioEstado = sismografo.getClass().getDeclaredField("actualCambioEstado");
            campoCambioEstado.setAccessible(true);
            Object cambioEstado = campoCambioEstado.get(sismografo);

            if (cambioEstado != null) {
                Field[] camposCambio = cambioEstado.getClass().getDeclaredFields();
                for (Field campo : camposCambio) {
                    campo.setAccessible(true);
                    Object valor = campo.get(cambioEstado);
                    String nombreCampo = "actualCambioEstado." + campo.getName();

                    // Formato legible
                    Object valorFormateado = formatearValor(valor);
                    datosList.add(new Object[]{nombreCampo, valorFormateado});
                }
            }

            // Convertir lista a array bidimensional
            Object[][] datos = new Object[datosList.size()][2];
            for (int i = 0; i < datosList.size(); i++) {
                datos[i] = datosList.get(i);
            }

            return datos;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    // Método auxiliar para formatear campos
    private static Object formatearValor(Object valor) {
        if (valor == null) {
            return "—";
        }

        if (valor instanceof LocalDateTime) {
            // Formato amigable para fechas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return ((LocalDateTime) valor).format(formatter);
        }

        // MotivoFueraDeServicio u otros objetos personalizados
        if (valor.getClass().getSimpleName().equals("MotivoFueraDeServicio")) {
            try {
                Field descripcionField = valor.getClass().getDeclaredField("descripcion");
                descripcionField.setAccessible(true);
                Object descripcion = descripcionField.get(valor);
                return descripcion != null ? descripcion.toString() : "—";
            } catch (Exception e) {
                return valor.toString();
            }
        }

        return valor;
    }
}
