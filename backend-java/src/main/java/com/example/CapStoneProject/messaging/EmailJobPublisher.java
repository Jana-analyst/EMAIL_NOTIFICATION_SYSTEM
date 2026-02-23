package com.example.CapStoneProject.messaging;

import com.example.CapStoneProject.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailJobPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EmailJobPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(UUID emailId) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_QUEUE,
                emailId.toString()
        );
    }
}