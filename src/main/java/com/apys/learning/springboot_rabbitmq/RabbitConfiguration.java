package com.apys.learning.springboot_rabbitmq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    //Создаем соединение с сервером
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    //Класс администратора для управления сервером обменника
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    //Шаблон Реббита
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    //Создание очереди и ее имени
    public Queue queue() {
        return new Queue("Queue");
    }

    @Bean
    public Queue queue1() {
        return new Queue("Queue1");
    }

// Секция создания Билдинга + Эксченжера для Fanout
    @Bean
    //Создаем Exchenger Fanout
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("common-exchange");
    }

    @Bean
    //Собственно делаем Биндинг Очереди к Ексченжеру Fanout
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(fanoutExchange());
    }

    @Bean
    //Собственно делаем Биндинг Очереди к Ексченжеру Fanout
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(fanoutExchange());
    }

// Секция создания Билдинга + Эксченжера для Direct
    @Bean
    //Создаем Exchenger Direct
    public DirectExchange directExchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    //Собственно делаем Биндинг Очереди к Ексченжеру Direct
    public Binding bindingD() {
        // С ключом error отправляем в очередь queue, а с warning и info в очередь queue1
        return BindingBuilder.bind(queue()).to(directExchange()).with("error");
    }

    @Bean
    //Собственно делаем Биндинг Очереди к Ексченжеру Direct
    public Binding bindingD1() {
        return BindingBuilder.bind(queue1()).to(directExchange()).with("warning");
    }

    @Bean
    //Собственно делаем Биндинг Очереди к Ексченжеру Direct
    public Binding bindingD2() {
        return BindingBuilder.bind(queue1()).to(directExchange()).with("info");
    }

// Секция создания Билдинга + Эксченжера для Topic
    @Bean
    //Создаем Exchenger Topic
    public TopicExchange topicExchange() {
        return new TopicExchange("topic-exchange");
    }

    @Bean
    //Собственно делаем Биндинг Очереди к Ексченжеру Topic
    public Binding bindingT() {
        // Вместо ключа как в Direct Exchange используется регулярное выражение (начинается с one)
        return BindingBuilder.bind(queue()).to(topicExchange()).with("one.*");
    }

    @Bean
    //Собственно делаем Биндинг Очереди к Ексченжеру Topic
    public Binding bindingT1() {
        // Вместо ключа как в Direct Exchange используется регулярное выражение (заканчивается second)
        return BindingBuilder.bind(queue1()).to(topicExchange()).with("*.second");
    }

//    //Собственно получатель (Consumer) сообщений ему указываем соединение, имя очереди и действие при получении сообщения (message)
//    //Это один из возможных вариантов Консюмера, но лучше использовать вариант Аннотаций
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer() {
//        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
//        listenerContainer.setConnectionFactory(connectionFactory());
//        listenerContainer.setQueueNames("Queue");
//        listenerContainer.setMessageListener(message -> logger.info("Полученная message from Queue: " + new String(message.getBody())));
//        return listenerContainer;
//    }
}
