package Modelos;

import Util.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class GestorCerrarOrdenInspeccion {

    // atributos
    private PantallaCerrarOrdenInspeccion pantallaCerrarOrdenInspeccion;
    private LocalDateTime fechaActual;
    private Sesion sesionActual;
    private Usuario usuarioActualSesion;
    private MotivoFueraDeServicio motivoSeleccionado;
    private String comentario;
    private Empleado empleadoActual;
    private List<OrdenInspeccion> listaOrdenesInspeccion;
    private List<OrdenInspeccion> listaOrdenesInspeccionEmpleado;
    private List<OrdenInspeccion> listaOrdenesInspeccionCompletaRealizada;
    private OrdenInspeccion seleccionada;
    private String observacionCierre;
    private List<String> listaTiposMotivos;

    // constructor
    public GestorCerrarOrdenInspeccion(PantallaCerrarOrdenInspeccion pantallaCerrarOrdenInspeccion, String rutaJson) {
        this.fechaActual = LocalDateTime.now();
        this.pantallaCerrarOrdenInspeccion = pantallaCerrarOrdenInspeccion;

        this.listaOrdenesInspeccion = new ArrayList<>();
        this.listaOrdenesInspeccionEmpleado = new ArrayList<>();
        this.listaOrdenesInspeccionCompletaRealizada = new ArrayList<>();

        this.seleccionada = null;
        this.listaTiposMotivos = new ArrayList<>();

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
    public List<OrdenInspeccion> opcionCerrarOrdenDeInspeccion() {
        empleadoActual = buscarEmpleado(sesionActual);

        buscarOrdenInspeccionRI(empleadoActual, listaOrdenesInspeccion);

        return ordenarOrdenInspeccionXFechaFinalizacion(listaOrdenesInspeccionCompletaRealizada);
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
        }

        return listaOrdenesInspeccionCompletaRealizada;
    }

    public List<OrdenInspeccion> ordenarOrdenInspeccionXFechaFinalizacion(List<OrdenInspeccion> listaOrdenesInspeccionCompletaRealizada) {
        listaOrdenesInspeccionCompletaRealizada.sort(Comparator.comparing(OrdenInspeccion::getFechaHoraFinalizacion));
        return listaOrdenesInspeccionCompletaRealizada;
    }

    public void tomarSeleccionOrdenInspeccion(OrdenInspeccion ordenSeleccionada) {
        System.out.println("Orden seleccionada por el usuario: " + ordenSeleccionada.getNroOrden());
        seleccionada = ordenSeleccionada;
    }

    public void tomarObservacionCierre(String textoObservacion) {
        System.out.println("Observacion de Cierre: " + textoObservacion);
        observacionCierre = textoObservacion;
    }

    public List<String> habilitarActualizarSituacionSismografo() {
        buscarTiposMotivosFueraServicio(listaTiposMotivos);
        return listaTiposMotivos;
    }

    public void buscarTiposMotivosFueraServicio(List<String> listaTiposMotivos) {
        List<MotivoTipo> listaMotivos = MotivoTipo.getMotivosTipo();
        for (MotivoTipo motivo : listaMotivos) {
            listaTiposMotivos.add(motivo.getDescripcion());
        }
    }
}

