package com.example.pedidos360_inventario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/inventario")
public class InventarioController {

    private final Map<String, Integer> stock = new HashMap<>();

    public InventarioController() {
        stock.put("Hamburguesa Triple", 50);
        stock.put("Papas Supremas", 100);
        stock.put("Bebida Grande", 80);
        stock.put("Pizza Familiar", 30);
        stock.put("Pastel de Chocolate", 20);
    }

    // Obtener todo el inventario
    @GetMapping
    public Map<String, Integer> consultarStock() {
        return stock;
    }

    // Consultar stock de un ítem específico
    @GetMapping("/{item}")
    public ResponseEntity<Map<String, Object>> consultarStockItem(@PathVariable String item) {
        Map<String, Object> respuesta = new HashMap<>();
        if (stock.containsKey(item)) {
            respuesta.put("producto", item);
            respuesta.put("disponible", stock.get(item));
            return ResponseEntity.ok(respuesta);
        }
        respuesta.put("mensaje", "Producto no encontrado en el inventario");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // Descontar stock (al realizar un pedido)
    @PostMapping("/descontar")
    public ResponseEntity<Map<String, String>> descontarStock(@RequestParam String item, @RequestParam int cantidad) {
        Map<String, String> respuesta = new HashMap<>();

        if (!stock.containsKey(item)) {
            respuesta.put("estado", "ERROR");
            respuesta.put("mensaje", "Producto no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }

        int stockActual = stock.get(item);
        if (stockActual < cantidad) {
            respuesta.put("estado", "RECHAZADO");
            respuesta.put("mensaje", "Stock insuficiente para " + item + ". Disponible: " + stockActual);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        stock.put(item, stockActual - cantidad);
        respuesta.put("estado", "OK");
        respuesta.put("mensaje", "Stock actualizado. Nuevo stock de " + item + ": " + (stockActual - cantidad));
        return ResponseEntity.ok(respuesta);
    }

    // Actualizar o reponer stock manualmente
    @PutMapping("/{item}")
    public String actualizarStock(@PathVariable String item, @RequestParam int cantidad) {
        stock.put(item, cantidad);
        return "Stock de " + item + " actualizado a " + cantidad;
    }
}
