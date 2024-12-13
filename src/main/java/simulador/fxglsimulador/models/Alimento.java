package simulador.fxglsimulador.models;

public class Alimento {
    private final String nombre;
    private boolean preparado;
    private final int tiempoPreparacion;

    public Alimento(String nombre, boolean preparado, int tiempoPreparacion) {
        this.nombre = nombre;
        this.preparado = preparado;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isPreparado() {
        return preparado;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setPreparado(boolean preparado) {
        this.preparado = preparado;
    }
}
