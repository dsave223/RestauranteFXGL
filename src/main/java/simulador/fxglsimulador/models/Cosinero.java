package simulador.fxglsimulador.models;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Cosinero {
    private final String nombre;
    private boolean estadoActual;
    private Pedido pedidoActual;
    private final Queue<Alimento> comidasEnPreparacion = new ConcurrentLinkedQueue<>();

    public Cosinero(String nombre, Pedido pedidoActual) {
        this.nombre = nombre;
        this.estadoActual = false;
        this.pedidoActual = null;
        this.comidasEnPreparacion.addAll(pedidoActual.getAlimentos());
    }

    public void asignarPedido(Pedido nuevoPedido) {
        if (estadoActual) {
            System.out.println("El cocinero " + nombre + " ya está ocupado.");
            return;
        }
        this.pedidoActual = nuevoPedido;
        this.comidasEnPreparacion.addAll(nuevoPedido.getAlimentos());
        this.estadoActual = true; // Ahora está ocupado
        System.out.println("El cocinero " + nombre + " ha recibido el pedido: " + nuevoPedido.getId());
    }

    public void prepararComida() {
        if (!estadoActual || comidasEnPreparacion.isEmpty()) {
            System.out.println("El cocinero " + nombre + " no tiene nada que preparar.");
            return;
        }

        // Sacar un alimento de la cola
        Alimento alimento = comidasEnPreparacion.poll();

        if (alimento != null) {
            System.out.println("El cocinero " + nombre + " está preparando: " + alimento.getNombre());
            try {
                // Simula el tiempo de preparación basado en la propiedad tiempoPreparacion
                Thread.sleep(alimento.getTiempoPreparacion() * 1000L); // Tiempo en milisegundos
            } catch (InterruptedException e) {
                System.out.println("La preparación fue interrumpida.");
            }
            System.out.println("El cocinero " + nombre + " terminó de preparar: " + alimento.getNombre());
            alimento.setPreparado(true);
        }

        // Verificar si el pedido ha sido completado
        if (comidasEnPreparacion.isEmpty()) {
            System.out.println("El cocinero " + nombre + " ha completado el pedido: " + pedidoActual.getId());
            estadoActual = false; // Ahora está libre
            pedidoActual = null;  // Libera el pedido actual
        }
    }

    public boolean isEstadoActual() {
        return estadoActual;
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public Queue<Alimento> getComidasEnPreparacion() {
        return comidasEnPreparacion;
    }
}