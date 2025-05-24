package Modelos;

public class EstacionSismologica {
    // atributos
    private String codigoEstacion;
    private String documentoCertificacionAdq;
    private String fechaSolicitudCertificacion;
    private double latitud;
    private double longitud;
    private String nombre;
    private String nroCertificacionAdquisicion;

    // constructor
    public EstacionSismologica(String codigoEstacion, String nombre, double lat, double lon) {
        this.codigoEstacion = codigoEstacion;
        this.nombre = nombre;
        this.latitud = lat;
        this.longitud = lon;
    }

    //getters


    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public String getDocumentoCertificacionAdq() {
        return documentoCertificacionAdq;
    }

    public String getFechaSolicitudCertificacion() {
        return fechaSolicitudCertificacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNroCertificacionAdquisicion() {
        return nroCertificacionAdquisicion;
    }

    @Override
    public String toString() {
        return "EstacionSismologica{" +
                "codigoEstacion='" + codigoEstacion + '\'' +
                ", documentoCertificacionAdq='" + documentoCertificacionAdq + '\'' +
                ", fechaSolicitudCertificacion='" + fechaSolicitudCertificacion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", nombre='" + nombre + '\'' +
                ", nroCertificacionAdquisicion='" + nroCertificacionAdquisicion + '\'' +
                '}';
    }
}
