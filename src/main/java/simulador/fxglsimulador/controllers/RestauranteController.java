package simulador.fxglsimulador.controllers;

import simulador.fxglsimulador.models.*;
import java.util.List;
import java.util.ArrayList;

public class RestauranteController {
    // Componentes principales del restaurante
    private final List<Mesa> mesas;
    private final List<Comensal> comensales;
    private final Recepcionista recepcionista;
    private final List<Mesero> meseros;
    private final List<Cosinero> cocineros;

    // Controladores específicos
    private final ClienteController clienteController;
    private final PedidoController pedidoController;

    // Configuraciones del restaurante
    private int capacidadMaximaRestaurante;
    private int tiempoSimulacion;

    public RestauranteController(int numeroDeMesas, int capacidadPorMesa,
                                 int numeroDeMeseros, int numeroDeCocineros) {
        // Inicializar mesas
        this.mesas = new ArrayList<>();
        for (int i = 1; i <= numeroDeMesas; i++) {
            mesas.add(new Mesa(i, capacidadPorMesa));
        }

        // Inicializar meseros
        this.meseros = new ArrayList<>();
        for (int i = 1; i <= numeroDeMeseros; i++) {
            meseros.add(new Mesero("Mesero " + i));
        }

        // Inicializar cocineros
        this.cocineros = new ArrayList<>();
        for (int i = 1; i <= numeroDeCocineros; i++) {
            // Inicialmente sin pedido
            cocineros.add(new Cosinero("Cocinero " + i, null));
        }

        // Inicializar recepcionista
        this.recepcionista = new Recepcionista("Recepcionista Principal", numeroDeMesas);

        // Inicializar lista de comensales
        this.comensales = new ArrayList<>();

        // Crear controladores
        this.clienteController = new ClienteController(recepcionista, mesas, meseros);
        this.pedidoController = new PedidoController(cocineros, meseros);

        // Configuraciones por defecto
        this.capacidadMaximaRestaurante = numeroDeMesas * capacidadPorMesa;
        this.tiempoSimulacion = 0;
    }

    // Método principal de simulación
    public void simularCiclo() {
        // Incrementar tiempo de simulación
        tiempoSimulacion++;

        // 1. Generar nuevos clientes (aleatoriamente)
        generarNuevosClientes();

        // 2. Procesar clientes en espera
        procesarClientesEnEspera();

        // 3. Preparar pedidos
        pedidoController.prepararPedidos();

        // 4. Entregar pedidos listos
        entregarPedidosListos();

        // 5. Simular tiempo de comida de clientes
        simularComidaClientes();

        // 6. Liberar mesas de clientes que terminaron
        liberarMesasTerminadas();
    }

    // Generar nuevos clientes de forma aleatoria
    private void generarNuevosClientes() {
        // Lógica para generar clientes con cierta probabilidad
        if (Math.random() < 0.3 && comensales.size() < capacidadMaximaRestaurante) {
            Comensal nuevoCliente = clienteController.crearCliente("Cliente " + (comensales.size() + 1));
            comensales.add(nuevoCliente);
            clienteController.asignarMesa(nuevoCliente);
        }
    }

    // Procesar clientes en lista de espera
    private void procesarClientesEnEspera() {
        List<Comensal> clientesEnEspera = new ArrayList<>(recepcionista.getClientesEnEspera());
        for (Comensal cliente : clientesEnEspera) {
            clienteController.asignarMesa(cliente);
        }
    }

    // Entregar pedidos listos
    private void entregarPedidosListos() {
        List<Pedido> pedidosListos = pedidoController.getPedidosListosParaEntregar();
        for (Pedido pedido : pedidosListos) {
            pedidoController.entregarPedido(pedido);
        }
    }

    // Simular tiempo de comida de clientes
    private void simularComidaClientes() {
        for (Comensal cliente : comensales) {
            if (cliente.getEstado() == Comensal.Estado.COMIENDO) {
                clienteController.simularComidaCliente(cliente);
            }
        }
    }

    // Liberar mesas de clientes que terminaron
    private void liberarMesasTerminadas() {
        List<Comensal> clientesTerminados = comensales.stream()
                .filter(c -> c.getEstado() == Comensal.Estado.TERMINADO)
                .toList();

        for (Comensal cliente : clientesTerminados) {
            // Buscar la mesa del cliente
            for (Mesa mesa : mesas) {
                if (mesa.getComensales() != null) {
                    for (Comensal c : mesa.getComensales()) {
                        if (c != null && c.equals(cliente)) {
                            clienteController.liberarMesa(cliente, mesa);
                            comensales.remove(cliente);
                            break;
                        }
                    }
                }
            }
        }
    }

    // Obtener estadísticas de la simulación
    public SimulacionEstadisticas obtenerEstadisticas() {
        return new SimulacionEstadisticas(
                tiempoSimulacion,
                comensales.size(),
                mesas.stream().filter(m -> m.getEstado() == Mesa.Estado.OCUPADA).count(),
                pedidoController.getPedidosActivos().size()
        );
    }

    // Clase interna para estadísticas de simulación
    public static class SimulacionEstadisticas {
        public final int tiempoSimulacion;
        public final int clientesActuales;
        public final long mesasOcupadas;
        public final int pedidosActivos;

        public SimulacionEstadisticas(int tiempoSimulacion, int clientesActuales,
                                      long mesasOcupadas, int pedidosActivos) {
            this.tiempoSimulacion = tiempoSimulacion;
            this.clientesActuales = clientesActuales;
            this.mesasOcupadas = mesasOcupadas;
            this.pedidosActivos = pedidosActivos;
        }
    }

    // Getters para acceso a componentes
    public List<Mesa> getMesas() {
        return mesas;
    }

    public List<Comensal> getComensales() {
        return comensales;
    }

    public Recepcionista getRecepcionista() {
        return recepcionista;
    }

    public List<Mesero> getMeseros() {
        return meseros;
    }

    public List<Cosinero> getCocineros() {
        return cocineros;
    }
}