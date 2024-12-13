package simulador.fxglsimulador.models;

import java.util.ArrayList;
import java.util.List;

public class Comensal {
    private final String nombre;
    private final List<Alimento> alimentos;
    private int tiempoRestante;
    private Estado estado;

    public Comensal(String nombre) {
        this.nombre = nombre;
        this.alimentos = new ArrayList<>();
        this.tiempoRestante = 0;
        this.estado = Estado.ESPERANDO;
    }

    public enum Estado {
        ESPERANDO,
        SIENDO_ATENDIDO,
        COMIENDO,
        TERMINADO
    }

    public void reducirTiempo(int tiempo) {
        tiempoRestante -= tiempo;
        if (tiempoRestante <= 0) {
            tiempoRestante = 0;
            estado = Estado.TERMINADO;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
