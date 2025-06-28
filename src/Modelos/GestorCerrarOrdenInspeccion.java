package Modelos;

import Util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GestorCerrarOrdenInspeccion {

    // atributos
    private PantallaCerrarOrdenInspeccion pantalla;
    private LocalDateTime fechaActual;
    private Sesion sesionActual;
    private Usuario usuarioActualSesion;
    private MotivoFueraDeServicio motivoSeleccionado;
    private String comentario;
    private Empleado empleadoActual;

    private List<OrdenInspeccion> listaOrdenesInspeccion;
    private List<OrdenInspeccion> listaOrdenesInspeccionEmpleado;
    private List<OrdenInspeccion> listaOrdenesInspeccionCompletaRealizada;
    private List<OrdenInspeccion> listaOrdenesInspeccionOrdenadas;
    private List<String[]> listaOrdenesInspeccionAMostrar;
    private OrdenInspeccion seleccionadaOrden;
    private String observacionCierre;
    private List<String> listaTiposMotivos;
    private List<String> listaMotivosSeleccionados;
    private List<String> listaComentarios;
    private String motivoTipoSeleccionado;
    private Sismografo sismografoSeleccionado;
    private List<String> listaMails;
    private InterfazMail interfazMail;
    private InterfazMonitorCCRS interfazMonitorCCRS;
    private boolean yaPidioConfirmacion;


    // constructor
    public GestorCerrarOrdenInspeccion(PantallaCerrarOrdenInspeccion pantallaCerrarOrdenInspeccion, InterfazMail interfazMail, InterfazMonitorCCRS interfazMonitorCCRS, String rutaJson) {
        this.fechaActual = null;
        this.pantalla = pantallaCerrarOrdenInspeccion;
        this.interfazMail = interfazMail;
        this.interfazMonitorCCRS = interfazMonitorCCRS;

        this.listaOrdenesInspeccion = new ArrayList<>();
        this.listaOrdenesInspeccionEmpleado = new ArrayList<>();
        this.listaOrdenesInspeccionCompletaRealizada = new ArrayList<>();
        this.listaOrdenesInspeccionOrdenadas = new ArrayList<>();
        this.listaOrdenesInspeccionAMostrar = new ArrayList<>();

        this.seleccionadaOrden = null;
        this.observacionCierre = null;
        this.listaTiposMotivos = new ArrayList<>();

        this.listaMotivosSeleccionados = new ArrayList<>();
        this.listaComentarios = new ArrayList<>();
        this.yaPidioConfirmacion = false;

        this.motivoTipoSeleccionado = null;

        this.sismografoSeleccionado = null;

        this.listaMails = new ArrayList<>();


        // Cargar desde JSON con Gson
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            FileReader reader = new FileReader(rutaJson);
            Type listType = new TypeToken<List<OrdenInspeccion>>() {}.getType();
            listaOrdenesInspeccion = gson.fromJson(reader, listType);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // prueba de sesion Actual
        LocalDateTime fechaInicio = LocalDateTime.parse("2025-05-23T12:30:00.123456789");
        this.sesionActual = new Sesion(new Usuario("Tomas", "abc123",
                new Empleado("tomi@gmail.com", "Gomez", "Tomas", "3518319242",
                        new Rol("Responsable Inscripcion", "Encargado de inscribir"))), fechaInicio, true);
    }


    public Sesion getSesionActual() {
        return sesionActual;
    }

    //metodos
    public void opcionCerrarOrdenDeInspeccion() {
        empleadoActual = buscarEmpleado(sesionActual);

        buscarOrdenInspeccionRI(empleadoActual, listaOrdenesInspeccion);
        listaOrdenesInspeccionOrdenadas = ordenarOrdenInspeccionXFechaFinalizacion(listaOrdenesInspeccionCompletaRealizada);

        // IMPLEMENTACION ALTERNATIVA 1
        if (listaOrdenesInspeccionOrdenadas.isEmpty()) {
            pantalla.mostrarMensajeSinOrdenes();
            return; // <- FLUJO ALTERNATIVO A1
        }

        listaOrdenesInspeccionAMostrar = obtenerDatosOrdenesInspeccionParaMostrar(listaOrdenesInspeccionOrdenadas);
        pantalla.mostrarOrdenesInspeccionCompletaRealizada(listaOrdenesInspeccionAMostrar);
    }


    public Empleado buscarEmpleado(Sesion sesionActual) {
        usuarioActualSesion = sesionActual.getUsuario();
        return usuarioActualSesion.getEmpleado();
    }

    public List<OrdenInspeccion> buscarOrdenInspeccionRI(Empleado empleadoActual, List<OrdenInspeccion> listaOrdenesInspeccion) {
        for (OrdenInspeccion ordenInspeccion : listaOrdenesInspeccion) {
            if (ordenInspeccion.esResponsable(empleadoActual)) {
                listaOrdenesInspeccionEmpleado.add(ordenInspeccion);
            }
        }
        for (OrdenInspeccion ordenInspeccion : listaOrdenesInspeccionEmpleado) {
            if (ordenInspeccion.getEstado().esCompletaRealizada()) {
                listaOrdenesInspeccionCompletaRealizada.add(ordenInspeccion);
            }

            List<Sismografo> listaSismografos = Sismografo.getTodosLosSismografos();
            for (Sismografo sismografo : listaSismografos) {
                if (sismografo.esEstacionSismologica(ordenInspeccion.getEstacionSismologica())) {
                    //System.out.println("Sismógrafo asignado: " + sismografo.getIdSismografo());
                    //System.out.println("Estacion asignada: " + ordenInspeccion.getEstacionSismologica().getCodigoEstacion());
                    ordenInspeccion.setSismografo(sismografo);
                }
            }

        }

        return listaOrdenesInspeccionCompletaRealizada;
    }


    public List<OrdenInspeccion> ordenarOrdenInspeccionXFechaFinalizacion(List<OrdenInspeccion> listaOrdenesInspeccionCompletaRealizada) {
        listaOrdenesInspeccionCompletaRealizada.sort(Comparator.comparing(OrdenInspeccion::getFechaHoraFinalizacion));
        return listaOrdenesInspeccionCompletaRealizada;
    }


    public List<String[]> obtenerDatosOrdenesInspeccionParaMostrar(List <OrdenInspeccion> listaOrdenesInspeccionOrdenadas) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<String[]> datosFormateados = new ArrayList<>();

        for (OrdenInspeccion o : listaOrdenesInspeccionOrdenadas) {
            if (o.getEstado().getNombreEstado().equals("CompletaRealizada")) {
                datosFormateados.add(new String[] {
                        String.valueOf(o.getNroOrden()),
                        o.getFechaHoraFinalizacion().format(formatter),
                        o.getEstacionSismologica().getCodigoEstacion(),
                        String.valueOf(o.getSismografo().getIdSismografo())
                });
            }
        }

        return datosFormateados;
    }


    public void tomarSeleccionOrdenInspeccion(int fila) {
        if (fila >= 0 && fila < listaOrdenesInspeccionOrdenadas.size()) {
            this.seleccionadaOrden = listaOrdenesInspeccionOrdenadas.get(fila);
            System.out.println("Seleccionaste orden: " + seleccionadaOrden.getNroOrden());
            pantalla.pedirActualizacionEstadoSismografo();
        } else {
            // Error defensivo
            System.err.println("Índice de fila fuera de rango: " + fila);
        }

    }

    public void tomarDecisionEstadoSismografo(boolean fueraDeServicio) {
        if (fueraDeServicio) {
        //    buscarTiposMotivosFueraServicio(listaTiposMotivos);
            pantalla.pedirIngresarObservacionCierre();
        } else {
            sismografoSeleccionado = seleccionadaOrden.getSismografo();
            fechaActual = getFechaActual();
            sismografoSeleccionado.actualizarSismografoAOnline(empleadoActual, fechaActual);
            System.out.println(sismografoSeleccionado.toString());
            finCU();
        }
    }

    public void tomarObservacionCierre(String textoObservacion) {
        // System.out.println("Observacion de Cierre: " + textoObservacion);
        observacionCierre = textoObservacion;
        habilitarActualizarSituacionSismografo();
    }

    public void habilitarActualizarSituacionSismografo() {
        buscarTiposMotivosFueraServicio(listaTiposMotivos);
        pantalla.mostrarMotivosFueraServicio(listaTiposMotivos);
    }

    public void buscarTiposMotivosFueraServicio(List<String> listaTiposMotivos) {
        List<MotivoTipo> listaMotivos = MotivoTipo.getMotivosTipo();
        for (MotivoTipo motivo : listaMotivos) {
            listaTiposMotivos.add(motivo.getDescripcion());
        }
    }

    public void tomarSeleccionMotivoComentario(List<String> motivosSeleccionados, List<String> comentarios) {
       listaMotivosSeleccionados = motivosSeleccionados;
       listaComentarios = comentarios;
       observacionCierre = comentarios.get(0);
       motivoTipoSeleccionado = motivosSeleccionados.get(0);

       if (!yaPidioConfirmacion) {
            pantalla.pedirConfirmacionCierreOrdenInspeccion();
            yaPidioConfirmacion = true;
        }
    }


    public void tomarConfirmacionCierreOrdenInspeccion() {
        if (!validarObservacionCierreOrdenExistente()) return;
        if (!validarMotivoSeleccionado()) return;

        actualizarOrdenACerrada();
    }

    private boolean validarObservacionCierreOrdenExistente() {
        if (observacionCierre == null || observacionCierre.trim().isEmpty()) {
            pantalla.mostrarError("Debe ingresar una observación de cierre."); // ✅ delega en pantalla
            return false;
        }
        return true;
    }

    private boolean validarMotivoSeleccionado() {
        if (listaMotivosSeleccionados == null || listaMotivosSeleccionados.isEmpty()) {
            pantalla.mostrarError("Debe seleccionar al menos un motivo de fuera de servicio.");
            return false;
        }

        for (int i = 0; i < listaMotivosSeleccionados.size(); i++) {
            String comentario = listaComentarios.get(i);
            if (comentario == null || comentario.trim().isEmpty()) {
                pantalla.mostrarError("Debe ingresar un comentario para el motivo: " + listaMotivosSeleccionados.get(i));
                return false;
            }
        }

        return true;
    }

    private void actualizarOrdenACerrada() {
        fechaActual = getFechaActual();
        //System.out.println(fechaActual);
        seleccionadaOrden.setFechaHoraCierre(fechaActual);

        seleccionadaOrden.cerrarOrden();

        MotivoTipo motivoSeleccionado = MotivoTipo.parse(motivoTipoSeleccionado);
        MotivoFueraDeServicio motivoFueraDeServicio = MotivoFueraDeServicio.parse(motivoSeleccionado, observacionCierre);
        sismografoSeleccionado = seleccionadaOrden.getSismografo();
        sismografoSeleccionado.actualizarSismografoAFueraServicio(empleadoActual, fechaActual, motivoFueraDeServicio);

        notificarEmpleadosReparacion();
    }

    public LocalDateTime getFechaActual() {
        return LocalDateTime.now();
    }


    private void notificarEmpleadosReparacion() {
        buscarEmpleadoRolReparacion();
        enviarNotificacion(listaMails, sismografoSeleccionado);
    }

    private void buscarEmpleadoRolReparacion() {
        List<Empleado> listaEmpleados = Empleado.getEmpleados();
        for (Empleado empleado : listaEmpleados) {
            if (empleado.esResponsableReparacion()) {
                listaMails.add(empleado.getMail());
            }
        }
    }

    private void enviarNotificacion(List<String> listaMails, Sismografo sismografoSeleccionado) {
        enviarMail(listaMails, sismografoSeleccionado);
        publicarMonitores(sismografoSeleccionado);
        //finCU();
    }

    public void enviarMail(List<String> listaMails, Sismografo sismografoSeleccionado) {
        interfazMail.notificarNovedadesImportadas(listaMails, sismografoSeleccionado);
    }

    public void publicarMonitores(Sismografo sismografoSeleccionado) {
        interfazMonitorCCRS.notificarNovedadesImportadas(sismografoSeleccionado);
    }

    private void finCU() {
        System.exit(0);
    }

}

