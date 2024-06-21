package com.bidder.notificationservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bidder.notificationservice.model.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}

