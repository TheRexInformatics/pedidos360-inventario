package com.example.pedidos360_inventario;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventarioConsumer {

    @KafkaListener(topics = "pedidos-inventario", groupId = "inventario-group")
    public void recibirDescuentoStock(String mensajeJson) {
        Map<String, String> mensaje = parsearJsonSimple(mensajeJson);

        System.out.println("<<< [KAFKA INVENTARIO] Mensaje recibido: " + mensajeJson);
        System.out.println("<<< Descontando cantidad " + mensaje.get("cantidad")
                + " del producto #" + mensaje.get("productoId")
                + " (Pedido #" + mensaje.get("idPedido") + ")");
    }

    private Map<String, String> parsearJsonSimple(String json) {
        Map<String, String> mapa = new HashMap<>();
        String limpio = json.replace("{", "").replace("}", "").replace("\"", "");
        for (String par : limpio.split(",")) {
            String[] kv = par.split(":");
            if (kv.length == 2) {
                mapa.put(kv[0].trim(), kv[1].trim());
            }
        }
        return mapa;
    }
}