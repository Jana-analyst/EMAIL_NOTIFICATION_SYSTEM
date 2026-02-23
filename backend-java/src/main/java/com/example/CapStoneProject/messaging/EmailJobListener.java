package com.example.CapStoneProject.messaging;

import com.example.CapStoneProject.configuration.RabbitMQConfig;
import com.example.CapStoneProject.service.WorkerService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailJobListener {

    private final WorkerService workerService;

    public EmailJobListener(WorkerService workerService) {
        this.workerService = workerService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handle(String emailId) {

        workerService.process(UUID.fromString(emailId));
    }
}