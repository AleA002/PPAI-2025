package Modelos;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PantallaCerrarOrdenInspeccion {
    private JPanel panelContenedor;
    private GestorCerrarOrdenInspeccion gestor;
    private JFrame ventana;
    private DefaultListModel<String> listModel;
    private JList<String> listaVisual;

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
        List <OrdenInspeccion> listaOrdenesInspeccionCompletaRealizada = gestor.opcionCerrarOrdenDeInspeccion();
        mostrarOrdenesInspeccionCompletaRealizada(listaOrdenesInspeccionCompletaRealizada, panelContenedor);
    }

    public void mostrarOrdenesInspeccionCompletaRealizada(List<OrdenInspeccion> listaOrdenesInspeccionCompletaRealizada, JPanel panelContenedor) {
        panelContenedor.removeAll();

        DefaultListModel<String> listModel = new DefaultListModel<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (OrdenInspeccion o : listaOrdenesInspeccionCompletaRealizada) {
            if (o.getEstado().getNombreEstado().equals("CompletaRealizada")) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\n");
                sb.append("  \"nroOrden\": ").append(o.getNroOrden()).append(",\n");
                sb.append("  \"fechaHoraInicio\": \"").append(o.getFechaHoraInicio().format(formatter)).append("\",\n");
                sb.append("  \"fechaHoraFinalizacion\": \"").append(o.getFechaHoraFinalizacion().format(formatter)).append("\",\n");
                sb.append("  \"fechaHoraCierre\": \"").append(o.getFechaHoraCierre().format(formatter)).append("\",\n");
                sb.append("  \"observacionCierre\": \"").append(o.getObservacionCierre()).append("\",\n");
                sb.append("  \"estado\": \"").append(o.getEstado().getNombreEstado()).append("\",\n");
                EstacionSismologica est = o.getEstacionSismologica();
                sb.append("  \"estacionSismologica\": {\n");
                sb.append("    \"codigoEstacion\": \"").append(est.getCodigoEstacion()).append("\",\n");
                sb.append("    \"documentoCertificacionAdj\": \"").append(est.getDocumentoCertificacionAdq()).append("\",\n");
                sb.append("    \"fechaSolicitudCertificacion\": \"").append(est.getFechaSolicitudCertificacion()).append("\",\n");
                sb.append("    \"latitud\": ").append(est.getLatitud()).append(",\n");
                sb.append("    \"longitud\": ").append(est.getLongitud()).append(",\n");
                sb.append("    \"nombre\": \"").append(est.getNombre()).append("\",\n");
                sb.append("    \"nroCertificacionAdquisicion\": \"").append(est.getNroCertificacionAdquisicion()).append("\"\n");
                sb.append("  },\n");

                sb.append("  \"empleado\": \"").append(o.getEmpleado().getNombre()).append(" ").append(o.getEmpleado().getApellido()).append("\"\n");
                sb.append("}");

                listModel.addElement(sb.toString());
            }
        }

        JList<String> lista = new JList<>(listModel);
        lista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JTextArea textArea = new JTextArea(value);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setOpaque(true);
            textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            textArea.setEditable(false);

            if (isSelected) {
                textArea.setBackground(list.getSelectionBackground());
                textArea.setForeground(list.getSelectionForeground());
            } else {
                textArea.setBackground(list.getBackground());
                textArea.setForeground(list.getForeground());
            }

            return textArea;
        });

        JScrollPane scrollPane = new JScrollPane(lista);
        scrollPane.setPreferredSize(new Dimension(780, 500));

        panelContenedor.setLayout(new BorderLayout());
        panelContenedor.add(scrollPane, BorderLayout.CENTER);
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }
}