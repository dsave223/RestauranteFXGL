package simulador.fxglsimulador.models;

public class Mesero {
    private final String nombre;
    private boolean ocupado;
    private Pedido pedidoActual;

    public Mesero(String nombre) {
        this.nombre = nombre;
        this.ocupado = false;
        this.pedidoActual = null;
    }

    public void atenderCliente(Comensal cliente, int numeroMesa) {
        if (ocupado) {
            System.out.println("El mesero " + nombre + " ya está ocupado.");
            return;
        }
        this.pedidoActual = new Pedido(cliente.getNombre(), numeroMesa, 0, cliente.getAlimentos());
        cliente.setEstado(Comensal.Estado.SIENDO_ATENDIDO);
        ocupado = true;
        System.out.println("El mesero " + nombre + " está atendiendo al cliente " + cliente.getNombre() + " en la mesa " + numeroMesa);
    }

    public void registrarPedido(Alimento alimento) {
        if (pedidoActual != null) {
            pedidoActual.agregarComida(alimento);
            System.out.println("El mesero " + nombre + " registró: " + alimento.getNombre() + " en el pedido.");
        } else {
            System.out.println("No hay un pedido activo para registrar.");
        }
    }

    public Pedido entregarPedido() {
        if (pedidoActual == null) {
            System.out.println("No hay un pedido para entregar.");
            return null;
        }
        System.out.println("El mesero " + nombre + " ha completado el pedido.");
        Pedido pedidoEntregado = this.pedidoActual;
        this.pedidoActual = null;
        ocupado = false;
        return pedidoEntregado;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }
}
