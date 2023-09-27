package com.apys.learning.springboot_rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SampleController {
    Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

//    // Это говоря грубо Exchanger Default
//    private final AmqpTemplate amqpTemplate;
//    @Autowired
//    public SampleController(AmqpTemplate amqpTemplate) {
//        this.amqpTemplate = amqpTemplate;
//    }

//    @PostMapping("/emit")
//    public ResponseEntity<String> emit(@RequestBody String message) {
//        logger.info("Emit to Queue");
//        for (int i = 0; i < 10; i++) {
//            //amqpTemplate.convertAndSend - Отправляем по шаблону в Exchenger Default
//            template.convertAndSend("Queue", message);
//        }
//        return ResponseEntity.ok("Success emit to Queue: " + message);
//    }

    // Это Рэббитовский шаблон, где мы уже будем указывать назначенный c Конфигурации Exchanger
    private final RabbitTemplate template;
    @Autowired
    public SampleController(RabbitTemplate template) {
        this.template = template;
    }

    @PostMapping("/emit")
    //Это для отправки сообщений через Fanout Exchenger запрос типа JSON:
    //{
    //  "message": "Hello!"
    //}
    public ResponseEntity<String> emit(@RequestBody String message) {
        logger.info("Emit to Fanout Queue");
        //Указываем Эксченжер
        template.setExchange("common-exchange");
        // Тут уже не надо указывать Очередь - она в приведена Биндингом к Эксченжеру
        template.convertAndSend(message);
        return ResponseEntity.ok("Success emit to Queue: " + message);
    }

    @PostMapping("/emit2")
    //Это для отправки сообщений через Direct Exchenger запрос типа JSON:
    //{
    //  "key": "error"
    //  "message": "Hello error!"
    //}
    public ResponseEntity<String> emit2(@RequestBody Map<String, String> map) {
        logger.info("Emit to Direct Queue");
        //Указываем Эксченжер
        template.setExchange("direct-exchange");
        // Тут уже не надо указывать Очередь - она в приведена Биндингом к Эксченжеру
        template.convertAndSend(map.get("key"), map.get("message"));
        return ResponseEntity.ok("Success emit to Queue");
    }

    @PostMapping("/emit3")
    //Это для отправки сообщений через Topic Exchenger запрос типа JSON:
    //{
    //  "key": "one.street"
    //  "message": "Hello One Street!"
    //}
    public ResponseEntity<String> emit3(@RequestBody Map<String, String> map) {
        logger.info("Emit to Topic Queue");
        //Указываем Эксченжер
        template.setExchange("topic-exchange");
        // Тут уже не надо указывать Очередь - она в приведена Биндингом к Эксченжеру
        template.convertAndSend(map.get("key"), map.get("message"));
        return ResponseEntity.ok("Success emit to Queue");
    }
}
