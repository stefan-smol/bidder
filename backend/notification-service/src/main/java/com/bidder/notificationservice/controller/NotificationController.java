package com.bidder.notificationservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidder.notificationservice.service.NotificationService;
import com.bidder.notificationservice.model.Notification;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	@MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public Notification send(@Payload Notification notification) {
        return notification;
    }

	@GetMapping
	public List<Notification> getAllNotifications() {
		return notificationService.findAll();
	}

	@PostMapping
	public Notification createNotification(@RequestBody Notification notification) {
		return notificationService.createAndBroadcastNotification(notification);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Notification> getNotificationById(@PathVariable String id) {
		return notificationService.findById(id).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Notification> updateNotification(@PathVariable String id,
			@RequestBody Notification notification) {
		return ResponseEntity.ok(notificationService.update(id, notification));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteNotification(@PathVariable String id) {
		notificationService.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
