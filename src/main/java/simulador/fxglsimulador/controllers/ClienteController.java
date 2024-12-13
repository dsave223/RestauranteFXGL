package simulador.fxglsimulador.controllers;

import simulador.fxglsimulador.models.Comensal;
import simulador.fxglsimulador.models.Mesa;
import simulador.fxglsimulador.models.Alimento;
import simulador.fxglsimulador.models.Recepcionista;
import simulador.fxglsimulador.models.Mesero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClienteController {
    private final Recepcionista recepcionista;
    private final List<Mesa> mesas;
    private final List<Mesero> meseros;
    private final Random random;

    public ClienteController(Recepcionista recepcionista, List<Mesa> mesas, List<Mesero> meseros) {
        this.recepcionista = recepcionista;
        this.mesas = mesas;
        this.meseros = meseros;
        this.random = new Random();
    }

    // Método para crear un nuevo cliente con un pedido aleatorio
    public Comensal crearCliente(String nombre) {
        Comensal cliente = new Comensal(nombre);
        cliente.getAlimentos().addAll(generarPedidoAleatorio());
        return cliente;
    }

    // Método para asignar una mesa al cliente
    public boolean asignarMesa(Comensal cliente) {
        for (Mesa mesa : mesas) {
            if (mesa.getEstado() != Mesa.Estado.OCUPADA) {
                if (mesa.agregarComensal(cliente)) {
                    recepcionista.recibirCliente(cliente);
                    asignarMesero(cliente, mesa);
                    return true;
                }
            }
        }
        return false;
    }

    // Método para asignar un mesero al cliente
    private void asignarMesero(Comensal cliente, Mesa mesa) {
        for (Mesero mesero : meseros) {
            if (!mesero.isOcupado()) {
                mesero.atenderCliente(cliente, mesa.getNumero());
                break;
            }
        }
    }

    // Genera un pedido aleatorio de alimentos
    private List<Alimento> generarPedidoAleatorio() {
        List<Alimento> menuDisponible = crearMenuBase();
        List<Alimento> pedido = new ArrayList<>();

        // Generar entre 1 y 3 platos aleatorios
        int cantidadPlatos = random.nextInt(3) + 1;

        for (int i = 0; i < cantidadPlatos; i++) {
            Alimento alimento = menuDisponible.get(random.nextInt(menuDisponible.size()));
            pedido.add(alimento);
        }

        return pedido;
    }

    // Método para crear un menú base de alimentos
    private List<Alimento> crearMenuBase() {
        List<Alimento> menu = new ArrayList<>();
        menu.add(new Alimento("Hamburguesa", false, 10));
        menu.add(new Alimento("Pizza", false, 15));
        menu.add(new Alimento("Ensalada", false, 5));
        menu.add(new Alimento("Pasta", false, 12));
        menu.add(new Alimento("Sopa", false, 8));
        return menu;
    }

    // Método para simular la comida del cliente
    public void simularComidaCliente(Comensal cliente) {
        // Tiempo de comida basado en los alimentos del pedido
        int tiempoTotal = cliente.getAlimentos().stream()
                .mapToInt(Alimento::getTiempoPreparacion)
                .sum() * 2; // Multiplicamos por 2 para simular tiempo de consumo

        cliente.setEstado(Comensal.Estado.COMIENDO);
        cliente.reducirTiempo(tiempoTotal);
    }

    // Método para liberar mesa cuando el cliente termina
    public void liberarMesa(Comensal cliente, Mesa mesa) {
        mesa.eliminarComensal(cliente);
        recepcionista.liberarMesa();
    }
}