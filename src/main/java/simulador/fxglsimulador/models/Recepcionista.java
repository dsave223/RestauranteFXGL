package simulador.fxglsimulador.models;

import java.util.LinkedList;
import java.util.Queue;

public class Recepcionista {
    private final String nombre;
    private final Queue<Comensal> clientesEnEspera;
    private int mesasDisponibles;

    public Recepcionista(String nombre, int mesasDisponibles) {
        this.nombre = nombre;
        this.clientesEnEspera = new LinkedList<>();
        this.mesasDisponibles = mesasDisponibles;
    }

    public void recibirCliente(Comensal cliente) {
        if (mesasDisponibles > 0) {
            System.out.println("La recepcionista " + nombre + " asignó una mesa a " + cliente.getNombre());
            cliente.setEstado(Comensal.Estado.SIENDO_ATENDIDO);
            mesasDisponibles--;
        } else {
            System.out.println("La recepcionista " + nombre + " añadió a " + cliente.getNombre() + " a la lista de espera.");
            clientesEnEspera.add(cliente);
        }
    }

    public void liberarMesa() {
        mesasDisponibles++;
        if (!clientesEnEspera.isEmpty()) {
            Comensal siguienteCliente = clientesEnEspera.poll();
            System.out.println("La recepcionista " + nombre + " asignó una mesa a " + siguienteCliente.getNombre());
            siguienteCliente.setEstado(Comensal.Estado.SIENDO_ATENDIDO);
            mesasDisponibles--;
        } else {
            System.out.println("Mesas disponibles: " + mesasDisponibles);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Queue<Comensal> getClientesEnEspera() {
        return clientesEnEspera;
    }

    public int getMesasDisponibles() {
        return mesasDisponibles;
    }
}
