package com.github.josiasdev.orderms.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    // Consumir fila e depois persistir
    public static final String ORDER_CREATED_QUEUE = "btg-pactual-order-created";
}
