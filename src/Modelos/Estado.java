package Modelos;

public class Estado {
    // atributos
    private String nombreEstado;
    private String ambito;


    // constructor
    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }
    // metodos
    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public boolean esCompletaRealizada() {
        if (nombreEstado.equals("CompletaRealizada")) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Estado{" +
                "nombreEstado='" + nombreEstado + '\'' +
                ", ambito='" + ambito + '\'' +
                '}';
    }
}
