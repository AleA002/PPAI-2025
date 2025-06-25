package Modelos;

import Util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;


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
        pantalla.mostrarOrdenesInspeccionCompletaRealizada(listaOrdenesInspeccionOrdenadas); //deberia ser String
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

    public void tomarSeleccionOrdenInspeccion(OrdenInspeccion ordenSeleccionada) {
        // System.out.println("Orden seleccionada por el usuario: " + ordenSeleccionada.getNroOrden());
        seleccionadaOrden = ordenSeleccionada;
        pantalla.pedirIngresarObservacionCierre();
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
        validarObservacionCierreOrdenExistente();
        validarMotivoSeleccionado(listaMotivosSeleccionados);
    }

    private void validarObservacionCierreOrdenExistente() {
        if (observacionCierre == null) {
            //System.out.println("No existe Observacion de Cierre de Orden");
            System.exit(0);
        }
    }

    private void validarMotivoSeleccionado(List<String> listaMotivosSeleccionados) {
        if (listaMotivosSeleccionados.size() < 1) {
            //System.out.println("No existe MotivosSeleccionados");
            System.exit(0);
        }
        
        actualizarOrdenACerrada();
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

