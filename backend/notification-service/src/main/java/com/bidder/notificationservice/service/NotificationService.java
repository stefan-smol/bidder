package com.bidder.notificationservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.bidder.notificationservice.model.Notification;
import com.bidder.notificationservice.repository.NotificationRepository;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    public void broadcastNotification(Notification notification) {
        System.out.println("Broadcasting notification: " + notification);
        messagingTemplate.convertAndSend("/topic/auction." + notification.getAuctionId(), notification);
    }
    
    public Notification createAndBroadcastNotification(Notification notification) {
        System.out.println("Creating and broadcasting notification: " + notification);
        Notification savedNotification = notificationRepository.save(notification);
        System.out.println("Saved notification: " + savedNotification);
        
        messagingTemplate.convertAndSend("/topic/auction." + notification.getAuctionId(), savedNotification);
        System.out.println("Broadcasted notification: " + savedNotification);
        return savedNotification;
    }
    
    public List<Notification> findAll() {
        List<Notification> notifications = notificationRepository.findAll();
        System.out.println("Finding all notifications. Total found: " + notifications.size());
        return notifications;
    }

    public Notification save(Notification notification) {
        System.out.println("Saving notification: " + notification);
        return notificationRepository.save(notification);
    }

    public Optional<Notification> findById(String id) {
        System.out.println("Finding notification by ID: " + id);
        return notificationRepository.findById(id);
    }

    public Notification update(String id, Notification notification) {
        System.out.println("Updating notification with ID: " + id);
        return notificationRepository.findById(id)
            .map(existingNotification -> {
                existingNotification.setBidAmount(notification.getBidAmount());
                existingNotification.setBidderUsername(notification.getBidderUsername());
                existingNotification.setAuctionId(notification.getAuctionId());
                existingNotification.setTimestamp(notification.getTimestamp());
                Notification updatedNotification = notificationRepository.save(existingNotification);
                System.out.println("Updated notification: " + updatedNotification);
                return updatedNotification;
            })
            .orElseGet(() -> {
                notification.setId(id);
                Notification savedNewNotification = notificationRepository.save(notification);
                System.out.println("Saved new notification as update: " + savedNewNotification);
                return savedNewNotification;
            });
    }

    public void deleteById(String id) {
        System.out.println("Deleting notification with ID: " + id);
        notificationRepository.deleteById(id);
    }
    
}
