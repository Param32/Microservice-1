package com.example.microservice1.config;

import com.example.microservice1.entities.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("microservice1_routing_json_key")
    private String routingJsonKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        LOGGER.info(String.format("Message sent -> %s", message));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void sendJsonMessage(Employee user){
        LOGGER.info(String.format("Json message sent -> %s", user.toString()));
        rabbitTemplate.convertAndSend(exchange, routingJsonKey, user);
    }

}
