package Modelos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sismografo {
    // atributos
    private int idSismografo;
    private LocalDateTime fechaAdquisicion;
    private String nroSerie;
    private EstacionSismologica estacionSismologica;
    private List<CambioEstado> cambiosDeEstado;
    private CambioEstado actualCambioEstado;

    // constructor principal
    public Sismografo(int id, LocalDateTime fechaAdq, String nroSerie, EstacionSismologica est, List<CambioEstado> cambiosDeEstado) {
        this.idSismografo = id;
        this.fechaAdquisicion = fechaAdq;
        this.nroSerie = nroSerie;
        this.estacionSismologica = est;
        this.cambiosDeEstado = new ArrayList<>();
    }

    // constructor alternativo (sin lista de cambios)
    public Sismografo(int id, LocalDateTime fechaAdq, String nroSerie, EstacionSismologica est) {
        this(id, fechaAdq, nroSerie, est, new ArrayList<>());
    }

    // getters
    public int getIdSismografo() {
        return idSismografo;
    }

    public LocalDateTime getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public EstacionSismologica getEstacionSismologica() {
        return estacionSismologica;
    }

    public List<CambioEstado> getCambiosDeEstado() {
        return cambiosDeEstado;
    }

    // métodos
    public void agregarCambioEstado(CambioEstado cambio) {
        cambiosDeEstado.add(cambio);
    }

    public static List<Sismografo> getTodosLosSismografos() {
        List<Sismografo> listaSismografos = new ArrayList<>();

        EstacionSismologica cba = new EstacionSismologica("EST-CBA-001", "Estación Sismológica Córdoba", -31.4167, -64.1833);
        EstacionSismologica ba = new EstacionSismologica("EST-BA-002", "Estación Sismológica Buenos Aires", -34.6037, -58.3816);
        EstacionSismologica mza = new EstacionSismologica("EST-MZA-003", "Estación Sismológica Mendoza", -32.8908, -68.8272);

        Rol responsable = new Rol("ResponsableReparacion", "Repara cosas");
        Empleado empleado1 = new Empleado("Juan", "Pérez", "juan.perez@mail.com", "3511111111", responsable);
        Empleado empleado2 = new Empleado("Ana", "Gómez", "ana.gomez@mail.com", "3512222222", responsable);
        Empleado empleado3 = new Empleado("Luis", "Fernández", "luis.fernandez@mail.com", "3513333333", responsable);

        Sismografo s1 = new Sismografo(1, LocalDateTime.of(2023, 1, 10, 10, 30), "4821-7643", cba);
        Sismografo s2 = new Sismografo(2, LocalDateTime.of(2023, 5, 15, 14, 45), "1359-9022", ba);
        Sismografo s3 = new Sismografo(3, LocalDateTime.of(2023, 7, 20, 9, 0), "7093-2144", mza);

        Estado operativo = new Estado("Operativo", "Sismografo");
        Estado fueraDeServicio = new Estado("Fuera de servicio", "Sismografo");
        Estado enOrdenInspeccion = new Estado("en Orden Inspeccion", "Sismografo");

        // Obtener el motivo "Mantenimiento programado" de la lista de MotivoTipo
        List<MotivoTipo> motivos = MotivoTipo.getMotivosTipo();
        MotivoTipo mantenimientoProgramado = motivos.stream()
                .filter(m -> m.getDescripcion().equals("Mantenimiento programado"))
                .findFirst()
                .orElse(null); // Podrías lanzar una excepción si esto es obligatorio

        MotivoFueraDeServicio motivo = new MotivoFueraDeServicio(
                mantenimientoProgramado,
                "Mantenimiento programado rutinario"
        );

        // S1
        s1.agregarCambioEstado(new CambioEstado(operativo, empleado1, LocalDateTime.of(2023, 1, 10, 10, 35)));
        s1.agregarCambioEstado(new CambioEstado(enOrdenInspeccion, empleado2, LocalDateTime.of(2023, 2, 12, 9, 10)));


        // S2
        s2.agregarCambioEstado(new CambioEstado(operativo, empleado1, LocalDateTime.of(2023, 5, 15, 14, 50)));
        s2.agregarCambioEstado(new CambioEstado(fueraDeServicio, empleado3, LocalDateTime.of(2023, 2, 18, 16, 40), motivo));

        // S3
        s3.agregarCambioEstado(new CambioEstado(operativo, empleado2, LocalDateTime.of(2023, 7, 20, 9, 5)));

        listaSismografos.add(s1);
        listaSismografos.add(s2);
        listaSismografos.add(s3);

        return listaSismografos;
    }

    public Boolean esEstacionSismologica(EstacionSismologica estacion) {
        return this.getEstacionSismologica().getCodigoEstacion().equals(estacion.getCodigoEstacion());
    }

    public void actualizarSismografoAFueraServicio(Empleado responsable, LocalDateTime fechaActual, MotivoFueraDeServicio motivoFueraDeServicio) {
        actualCambioEstado = buscarEstadoActual();

        actualCambioEstado.setFechaHoraFin(fechaActual);

        actualCambioEstado = this.crearCambioEstadoFS(fechaActual, responsable, motivoFueraDeServicio);
        // System.out.println(actualCambioEstado.toString());
    }

    public void actualizarSismografoAOnline(Empleado responsable, LocalDateTime fechaActual) {
        actualCambioEstado = buscarEstadoActual();

        actualCambioEstado.setFechaHoraFin(fechaActual);

        actualCambioEstado = this.crearCambioEstadoON(responsable, fechaActual);
    }


    public CambioEstado crearCambioEstadoFS(LocalDateTime fechaHoraInicio, Empleado responsable, MotivoFueraDeServicio motivoFueraDeServicio) { //metodo del Sismografo
        CambioEstado nuevoCambioEstado = new CambioEstado(new Estado("Fuera de Servicio", "Sismografo"), responsable, fechaHoraInicio, motivoFueraDeServicio);
        cambiosDeEstado.add(nuevoCambioEstado);
        return nuevoCambioEstado;

    }

    public CambioEstado crearCambioEstadoON(Empleado responsable, LocalDateTime fechaHoraInicio) {
        System.out.println("CAMBIO DE ESTADO CREADO");
        CambioEstado nuevoCambioEstado = new CambioEstado(new Estado("Online", "Sismografo"), responsable, fechaHoraInicio);
        cambiosDeEstado.add(nuevoCambioEstado);
        return nuevoCambioEstado;

    }

    public CambioEstado buscarEstadoActual() {
        for (CambioEstado cambioEstado : this.getCambiosDeEstado()) {
            // System.out.println(cambioEstado);
            if (cambioEstado.sosActual()) {
                return cambioEstado;
            }
        }
        return null;
    }

    //to String

    @Override
    public String toString() {
        return "Sismografo{" +
                "idSismografo=" + idSismografo +
                ", fechaAdquisicion=" + fechaAdquisicion +
                ", nroSerie='" + nroSerie + '\'' +
                ", estacionSismologica=" + estacionSismologica +
                ", cambiosDeEstado=" + cambiosDeEstado +
                ", actualCambioEstado=" + actualCambioEstado +
                '}';
    }
}
