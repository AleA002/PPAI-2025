package Modelos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PantallaCerrarOrdenInspeccion {
    private JPanel panelContenedor;
    private GestorCerrarOrdenInspeccion gestor;
    private JFrame ventana;
    private List<OrdenInspeccion> ordenesVisibles;
    private JTable tablaOrdenes;
    private DefaultListModel<String> listModel;
    private JList<String> listaVisual;
    private JTextArea campoObservacion;
    private JButton botonConfirmar;
    public PantallaCerrarOrdenInspeccion(String rutaJson) {
        this.gestor = new GestorCerrarOrdenInspeccion(this, rutaJson);
    }

    public void habilitarPantalla() {
        ventana = new JFrame("Lista Ordenes de Inspeccion Completa Realizada");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setSize(800, 600);
        ventana.setLayout(new BorderLayout());

        panelContenedor = new JPanel();
        panelContenedor.setLayout(new BorderLayout());
        ventana.add(panelContenedor, BorderLayout.CENTER);

        ventana.setVisible(true);
    }

    public void tomarOpcionCerrarOrdenDeInspeccion() {
        habilitarPantalla();
        gestor.opcionCerrarOrdenDeInspeccion();
    }

    public void mostrarOrdenesInspeccionCompletaRealizada(List<OrdenInspeccion> listaOrdenesInspeccionCompletaRealizada) {
        panelContenedor.removeAll();

        ordenesVisibles = new ArrayList<>();
        List<Object[]> datosTabla = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (OrdenInspeccion o : listaOrdenesInspeccionCompletaRealizada) {
            if (o.getEstado().getNombreEstado().equals("CompletaRealizada")) {
                ordenesVisibles.add(o);
                datosTabla.add(new Object[]{
                        o.getNroOrden(),
                        o.getFechaHoraInicio().format(formatter),
                        o.getFechaHoraFinalizacion().format(formatter),
                        o.getFechaHoraCierre().format(formatter),
                        o.getObservacionCierre(),
                        o.getEstado().getNombreEstado(),
                        o.getEstacionSismologica().getCodigoEstacion(),
                        o.getEmpleado().getNombre() + " " + o.getEmpleado().getApellido()
                });
            }
        }

        String[] columnas = {
                "Nro Orden", "Inicio", "Fin", "Cierre", "Observación Cierre",
                "Estado", "Código Estación", "Empleado"
        };

        Object[][] datos = datosTabla.toArray(new Object[0][]);

        tablaOrdenes = new JTable(new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                super.setValueAt(value, row, column);
                if (column == 4) {
                    ordenesVisibles.get(row).setObservacionCierre(value.toString());
                }
            }
        });

        tablaOrdenes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tablaOrdenes.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(tablaOrdenes);
        scrollPane.setPreferredSize(new Dimension(780, 500));

        JButton botonSeleccionar = new JButton("Seleccionar Orden de Inspección");
        botonSeleccionar.addActionListener(e -> pedirSeleccionOrdenInspeccion());

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(botonSeleccionar);

        panelContenedor.setLayout(new BorderLayout());
        panelContenedor.add(scrollPane, BorderLayout.CENTER);
        panelContenedor.add(panelInferior, BorderLayout.SOUTH);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    public void pedirSeleccionOrdenInspeccion() {
        int filaSeleccionada = tablaOrdenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(ventana, "Por favor, seleccione una orden de inspección.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tomarSeleccionOrdenInspeccion(filaSeleccionada);
    }

    public void tomarSeleccionOrdenInspeccion(int fila) {
        OrdenInspeccion ordenSeleccionada = ordenesVisibles.get(fila);
        // Acá hacé lo que necesites con la orden seleccionada
        JOptionPane.showMessageDialog(ventana, "Orden seleccionada:\nNro: " + ordenSeleccionada.getNroOrden(), "Orden Seleccionada", JOptionPane.INFORMATION_MESSAGE);

        gestor.tomarSeleccionOrdenInspeccion(ordenSeleccionada);

    }

    public void pedirIngresarObservacionCierre() {
        panelContenedor.removeAll();

        JLabel label = new JLabel("Ingrese la observación de cierre para la Orden");
        label.setFont(new Font("Monospaced", Font.BOLD, 14));

        campoObservacion = new JTextArea(5, 40);
        campoObservacion.setLineWrap(true);
        campoObservacion.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(campoObservacion);

        botonConfirmar = new JButton("Confirmar Observación");
        botonConfirmar.addActionListener(e -> tomarObservacionCierre());

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.add(label);
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(scroll);
        panelForm.add(Box.createVerticalStrut(20));
        panelForm.add(botonConfirmar);

        panelContenedor.add(panelForm, BorderLayout.CENTER);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    public void tomarObservacionCierre() {
        String textoObservacion = campoObservacion.getText().trim();

        if (textoObservacion.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "La observación no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        gestor.tomarObservacionCierre(textoObservacion);

        JOptionPane.showMessageDialog(ventana, "Observación guardada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);


    }

    public void mostrarMotivosFueraServicio(List<String> listaMotivos) {
        panelContenedor.removeAll();

        JLabel label = new JLabel("Seleccione los motivos de fuera de servicio:");
        label.setFont(new Font("Monospaced", Font.BOLD, 14));

        String[] columnNames = { "Motivo", "Seleccionado", "Comentario" };
        Object[][] data = new Object[listaMotivos.size()][3];
        for (int i = 0; i < listaMotivos.size(); i++) {
            data[i][0] = listaMotivos.get(i);
            data[i][1] = false;
            data[i][2] = "";
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 1 -> Boolean.class;
                    default -> String.class;
                };
            }
        };

        JTable tablaMotivos = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(tablaMotivos);

        JButton botonConfirmarMotivo = new JButton("Confirmar motivo");
        botonConfirmarMotivo.addActionListener(e -> {
            pedirSeleccionarMotivosFueraServicio(tableModel);
        });

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.add(label);
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(scroll);
        panelForm.add(Box.createVerticalStrut(20));
        panelForm.add(botonConfirmarMotivo);

        panelContenedor.add(panelForm, BorderLayout.CENTER);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }


    private void pedirSeleccionarMotivosFueraServicio(DefaultTableModel tableModel) {
        List<String> motivosSeleccionados = new ArrayList<>();
        List<String> comentarios = new ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tomarSeleccionMotivoComentario(i, tableModel, motivosSeleccionados, comentarios);
        }

        if (motivosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                    "Debe seleccionar al menos un motivo.",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // este ciclo es solo para probar - BORRARLO DESPUES!!!!!!
        for (int j = 0; j < motivosSeleccionados.size(); j++) {
            System.out.println("Motivo: " + motivosSeleccionados.get(j));
            System.out.println("Comentario: " + comentarios.get(j));
        }

        // Acá podrías seguir con la lógica de guardado o procesamiento
    }

    public void tomarSeleccionMotivoComentario(int i, DefaultTableModel tableModel, List<String> motivosSeleccionados, List<String> comentarios) {
        Boolean seleccionado = (Boolean) tableModel.getValueAt(i, 1);
        if (seleccionado != null && seleccionado) {
            String motivo = (String) tableModel.getValueAt(i, 0);
            String comentario = (String) tableModel.getValueAt(i, 2);

        if (comentario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                    "Debe ingresar un comentario para el motivo: " + motivo,
                    "Comentario requerido",
                    JOptionPane.WARNING_MESSAGE);
        }

        motivosSeleccionados.add(motivo);
        comentarios.add(comentario);

        gestor.tomarSeleccionMotivoComentario(motivosSeleccionados, comentarios);
        }
    }

    public void pedirConfirmacionCierreOrdenInspeccion() {
        int opcion = JOptionPane.showOptionDialog(
                ventana, // o null si no tenés un JFrame específico
                "¿Confirmar Cierre de Orden de Inspección?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[] { "SI", "NO" },
                "NO"
        );
        tomarConfirmacionCierreOrdenInspeccion(opcion);

    }

    public void tomarConfirmacionCierreOrdenInspeccion(int opcion) {
        if (opcion == JOptionPane.NO_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            System.exit(0); // Finaliza el programa
        }
        gestor.tomarConfirmacionCierreOrdenInspeccion(); //Continua la ejecucion
    }

}