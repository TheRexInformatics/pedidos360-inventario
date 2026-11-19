package com.example.pedidos360_inventario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class InventarioConsumer {

    @RabbitListener(queues = RabbitMQConfig.COLA_INVENTARIO)
    public void recibirDescuentoStock(Map<String, Object> mensaje) {
        System.out.println("<<< [RABBITMQ INVENTARIO] Mensaje recibido: " + mensaje);
        System.out.println("<<< Descontando cantidad " + mensaje.get("cantidad") + " del producto #" + mensaje.get("productoId") + " (Pedido #" + mensaje.get("idPedido") + ")");
    }
}