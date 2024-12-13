package simulador.fxglsimulador.controllers;

import simulador.fxglsimulador.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoController {
    private final List<Pedido> pedidosActivos;
    private final List<Cosinero> cocineros;
    private final List<Mesero> meseros;

    public PedidoController(List<Cosinero> cocineros, List<Mesero> meseros) {
        this.pedidosActivos = new ArrayList<>();
        this.cocineros = cocineros;
        this.meseros = meseros;
    }

    // Método para crear un nuevo pedido
    public Pedido crearPedido(Comensal comensal, int numeroMesa) {
        Pedido nuevoPedido = new Pedido(
                comensal.getNombre(),
                numeroMesa,
                calcularTiempoPedido(comensal.getAlimentos()),
                comensal.getAlimentos()
        );

        pedidosActivos.add(nuevoPedido);
        return nuevoPedido;
    }

    // Calcular tiempo total de preparación del pedido
    private int calcularTiempoPedido(List<Alimento> alimentos) {
        return alimentos.stream()
                .mapToInt(Alimento::getTiempoPreparacion)
                .sum();
    }

    // Asignar pedido a un cocinero disponible
    public boolean asignarPedidoACocinero(Pedido pedido) {
        for (Cosinero cocinero : cocineros) {
            if (!cocinero.isEstadoActual()) {
                cocinero.asignarPedido(pedido);
                pedido.setEstado("EN_PREPARACION");
                return true;
            }
        }
        return false;
    }

    // Método para preparar pedidos
    public void prepararPedidos() {
        cocineros.forEach(Cosinero::prepararComida);
    }

    // Verificar pedidos listos para entregar
    public List<Pedido> getPedidosListosParaEntregar() {
        return pedidosActivos.stream()
                .filter(pedido -> pedido.getEstado().equals("EN_PREPARACION")
                        && todosAlimentosPreparados(pedido))
                .collect(Collectors.toList());
    }

    // Verificar si todos los alimentos de un pedido están preparados
    private boolean todosAlimentosPreparados(Pedido pedido) {
        return pedido.getAlimentos().stream()
                .allMatch(Alimento::isPreparado);
    }

    // Entregar pedido a un mesero
    public boolean entregarPedido(Pedido pedido) {
        for (Mesero mesero : meseros) {
            if (!mesero.isOcupado()) {
                // Asignar pedido al mesero
                mesero.registrarPedido(pedido.getAlimentos().get(0));

                // Marcar pedido como entregado
                pedido.setEstado("ENTREGADO");
                pedidosActivos.remove(pedido);

                return true;
            }
        }
        return false;
    }

    // Obtener pedidos pendientes
    public List<Pedido> getPedidosPendientes() {
        return pedidosActivos.stream()
                .filter(pedido -> pedido.getEstado().equals("PENDIENTE"))
                .collect(Collectors.toList());
    }

    // Cancelar pedido
    public boolean cancelarPedido(Pedido pedido) {
        if (pedidosActivos.remove(pedido)) {
            pedido.setEstado("CANCELADO");
            return true;
        }
        return false;
    }

    // Método para actualizar estado de pedidos
    public void actualizarEstadoPedidos() {
        pedidosActivos.forEach(pedido -> {
            // Lógica para actualizar estados de pedidos
            if (todosAlimentosPreparados(pedido)) {
                pedido.setEstado("LISTO_PARA_ENTREGAR");
            }
        });
    }

    // Obtener pedido por ID
    public Pedido getPedidoPorId(String id) {
        return pedidosActivos.stream()
                .filter(pedido -> pedido.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Obtener todos los pedidos activos
    public List<Pedido> getPedidosActivos() {
        return new ArrayList<>(pedidosActivos);
    }
}
