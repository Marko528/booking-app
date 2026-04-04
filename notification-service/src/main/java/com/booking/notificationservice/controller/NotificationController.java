package com.booking.notificationservice.controller;

import com.booking.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<String>> getAllNotifications() {
        log.info("GET /api/notifications - fetching all");
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody String message) {
        log.info("POST /api/notifications/send");
        notificationService.sendNotification(message);
        return ResponseEntity.ok("Notification sent: " + message);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service is UP");
    }
}