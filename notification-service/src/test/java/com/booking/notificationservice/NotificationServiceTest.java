package com.booking.notificationservice;

import com.booking.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
    }

    @Test
    void testSendNotification() {
        notificationService.sendNotification("Test booking notification");
        List<String> notifications = notificationService.getAllNotifications();
        assertFalse(notifications.isEmpty());
    }

    @Test
    void testGetAllNotifications() {
        notificationService.sendNotification("Booking 1 created");
        notificationService.sendNotification("Booking 2 created");
        List<String> notifications = notificationService.getAllNotifications();
        assertEquals(2, notifications.size());
    }

    @Test
    void testNotificationFormat() {
        notificationService.sendNotification("user 1 booked property 2");
        List<String> notifications = notificationService.getAllNotifications();
        assertTrue(notifications.get(0).startsWith("[NOTIFICATION]"));
    }

    @Test
    void testReceiveMessage() {
        notificationService.receiveMessage("Booking created for user 1");
        List<String> notifications = notificationService.getAllNotifications();
        assertFalse(notifications.isEmpty());
    }
}