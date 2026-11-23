package com.example.pedidos360_inventario;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1/inventario")
public class InventarioController {

    // Inicia vacío: sin productos por defecto
    private final Map<String, Integer> stockProductos = new ConcurrentHashMap<>();
    private final Map<Long, String> productosPorId = new ConcurrentHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(1);

    @GetMapping
    public Map<String, Integer> obtenerInventario() {
        return stockProductos;
    }

    @PostMapping
    public Map<String, Object> agregarProductoInventario(@RequestBody Map<String, Object> body) {
        String nombre = String.valueOf(body.getOrDefault("producto", body.getOrDefault("nombre", ""))).trim();
        int stock = Integer.parseInt(String.valueOf(body.getOrDefault("stock", body.getOrDefault("cantidad", 0))));

        if (!nombre.isEmpty() && stock >= 0) {
            stockProductos.merge(nombre, stock, Integer::sum);
            if (!productosPorId.containsValue(nombre)) {
                productosPorId.put(secuenciaId.getAndIncrement(), nombre);
            }
        }

        return Map.of(
            "mensaje", "Producto registrado en inventario",
            "producto", nombre,
            "stockActual", stockProductos.getOrDefault(nombre, 0),
            "inventario", stockProductos
        );
    }

    public void descontarPorId(Long productoId, int cantidad) {
        String nombre = productosPorId.get(productoId);
        if (nombre != null) {
            stockProductos.computeIfPresent(nombre, (k, actual) -> Math.max(0, actual - cantidad));
            System.out.println(">>> [INVENTARIO] Descontadas " + cantidad + " unidades de " + nombre + " (ID " + productoId + ")");
        } else if (!stockProductos.isEmpty()) {
            String primerProducto = stockProductos.keySet().iterator().next();
            stockProductos.computeIfPresent(primerProducto, (k, actual) -> Math.max(0, actual - cantidad));
            System.out.println(">>> [INVENTARIO] Descontadas " + cantidad + " unidades de " + primerProducto);
        }
    }

    public void descontarStock(String producto, int cantidad) {
        stockProductos.computeIfPresent(producto, (k, actual) -> Math.max(0, actual - cantidad));
    }
}
