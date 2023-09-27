package com.apys.learning.springboot_rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {
    Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    @RabbitListener(queues = "Queue")
    //Consumer - один из вариантов
    public void processQueue(String message) {
        logger.info("Приняли сообщение через новый Consumer: {}", message);
    }

    @RabbitListener(queues = "Queue1")
    //Consumer2 - подключили второго слушателя
    public void processQueue2(String message) {
        logger.info("Приняли сообщение через новый Consumer2: {}", message);
    }
}
