package com.bidder.auctionprocessingservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FORWARD_QUEUE = "forward_queue";
    public static final String DUTCH_QUEUE = "dutch_queue";
    public static final String EXCHANGE = "message_exchange";
    public static final String FORWARD_ROUTING_KEY = "forward_routing_key";
    public static final String DUTCH_ROUTING_KEY = "dutch_routing_key";

    @Bean
    Queue forwardQueue() {
        return new Queue(FORWARD_QUEUE, true);
    }

    @Bean
    Queue dutchQueue() {
        return new Queue(DUTCH_QUEUE, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Binding bindingForwardQueue() {
        return BindingBuilder
                .bind(forwardQueue())
                .to(exchange())
                .with(FORWARD_ROUTING_KEY);
    }

    @Bean
    Binding bindingDutchQueue() {
        return BindingBuilder
                .bind(dutchQueue())
                .to(exchange())
                .with(DUTCH_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
