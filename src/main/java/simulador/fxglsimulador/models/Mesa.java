package simulador.fxglsimulador.models;

public class Mesa {
    private final int numero;
    private final int capacidadMaxima;
    private int comensalesActuales;
    private Estado estado;
    private Comensal[] comensales;

    public Mesa(int numero, int capacidadMaxima) {
        this.numero = numero;
        this.capacidadMaxima = capacidadMaxima;
        this.comensalesActuales = 0;
        this.estado = Estado.LIBRE;
        this.comensales = new Comensal[capacidadMaxima];
    }

    public enum Estado {
        LIBRE,
        OCUPADA,
        PARCIALMENTE_OCUPADA
    }

    public boolean agregarComensal(Comensal comensal) {
        if (comensalesActuales < capacidadMaxima) {
            comensales[comensalesActuales] = comensal;
            comensalesActuales++;

            // Actualizar el estado de la mesa
            if (comensalesActuales == capacidadMaxima) {
                estado = Estado.OCUPADA;
            } else {
                estado = Estado.PARCIALMENTE_OCUPADA;
            }

            return true;
        }
        return false;
    }

    public boolean eliminarComensal(Comensal comensal) {
        for (int i = 0; i < comensalesActuales; i++) {
            if (comensales[i].equals(comensal)) {
                // Desplazar los comensales restantes
                for (int j = i; j < comensalesActuales - 1; j++) {
                    comensales[j] = comensales[j + 1];
                }
                comensales[comensalesActuales - 1] = null;
                comensalesActuales--;

                // Actualizar el estado de la mesa
                if (comensalesActuales == 0) {
                    estado = Estado.LIBRE;
                } else {
                    estado = Estado.PARCIALMENTE_OCUPADA;
                }

                return true;
            }
        }
        return false;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getComensalesActuales() {
        return comensalesActuales;
    }

    public Estado getEstado() {
        return estado;
    }

    public Comensal[] getComensales() {
        return comensales;
    }
}
