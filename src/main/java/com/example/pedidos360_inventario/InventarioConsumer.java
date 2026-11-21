package com.example.pedidos360_inventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventarioConsumer {

    @Autowired
    private InventarioController inventarioController;

    @KafkaListener(topics = "pedidos-inventario", groupId = "inventario-group")
    public void recibirDescuentoStock(String mensajeJson) {
        Map<String, String> mensaje = parsearJsonSimple(mensajeJson);

        Long productoId = Long.parseLong(mensaje.getOrDefault("productoId", "1"));
        int cantidad = Integer.parseInt(mensaje.getOrDefault("cantidad", "1"));

        System.out.println("<<< [KAFKA INVENTARIO] Mensaje recibido: " + mensajeJson);
        inventarioController.descontarPorId(productoId, cantidad);
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