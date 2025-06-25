package Modelos;

public class MotivoFueraDeServicio {
    // atributos
    private String comentario;
    private MotivoTipo motivoTipo;


    // constructor
    public MotivoFueraDeServicio(MotivoTipo motivoTipo, String comentario) {
        this.motivoTipo = motivoTipo;
        this.comentario = comentario;
    }

    public static MotivoFueraDeServicio parse(MotivoTipo motivoTipo, String comentario) {
        return new MotivoFueraDeServicio(motivoTipo, comentario);
    }

    @Override
    public String toString() {
        return "MotivoFueraDeServicio{" +
                "comentario='" + comentario + '\'' +
                ", motivoTipo=" + motivoTipo +
                '}';
    }
}
