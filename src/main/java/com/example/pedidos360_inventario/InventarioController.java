// Mantén tu línea "package ...;" original aquí arriba

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/v1/inventario")
public class InventarioController {

    // Inicia vacío: sin productos por defecto
    private final Map<String, Integer> stockProductos = new ConcurrentHashMap<>();

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
        }

        return Map.of(
            "mensaje", "Producto registrado en inventario",
            "producto", nombre,
            "stockActual", stockProductos.getOrDefault(nombre, 0),
            "inventario", stockProductos
        );
    }
}
