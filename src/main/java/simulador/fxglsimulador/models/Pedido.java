package simulador.fxglsimulador.models;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String id;
    private int tiempo;
    private final List<Alimento> alimentos;
    private String estado;

    public Pedido(String nombreCliente, int numeroMesa, int tiempo, List<Alimento> alimentos) {
        this.id = "Mesa" + numeroMesa + "_" + nombreCliente;
        this.tiempo = tiempo;
        this.alimentos = new ArrayList<>(alimentos);
        this.estado = "PENDIENTE";
    }

    public void agregarComida(Alimento alimento) {
        alimentos.add(alimento);
        recalcularTiempo();
    }

    public void recalcularTiempo() {
        this.tiempo = alimentos.stream().mapToInt(Alimento::getTiempoPreparacion).sum();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", tiempo=" + tiempo +
                ", alimentos=" + alimentos +
                '}';
    }

    public String getId() {
        return id;
    }

    public int getTiempo() {
        return tiempo;
    }

    public List<Alimento> getAlimentos() {
        return alimentos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
