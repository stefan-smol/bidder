package com.bidder.auctionprocessingservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidder.auctionprocessingservice.config.RabbitMQConfig;

@Service
public class MessagePublisher {
	
	public enum AuctionType {
        FORWARD, DUTCH
    }

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Object message, AuctionType auctionType) {
        String routingKey;
        
        switch (auctionType) {
            case FORWARD:
                routingKey = RabbitMQConfig.FORWARD_ROUTING_KEY;
                break;
            case DUTCH:
                routingKey = RabbitMQConfig.DUTCH_ROUTING_KEY;
                break;
            default:
                throw new IllegalArgumentException("Unsupported auction type: " + auctionType);
        }

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, routingKey, message);
    }
}