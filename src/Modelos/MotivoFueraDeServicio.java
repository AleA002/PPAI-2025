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

}
