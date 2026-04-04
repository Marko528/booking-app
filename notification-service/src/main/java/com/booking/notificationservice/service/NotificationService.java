package com.booking.notificationservice.service;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NotificationService {

    // RxJava - reaktivna komunikacija
    private final PublishSubject<String> notificationSubject = PublishSubject.create();
    private final List<String> notificationLog = new ArrayList<>();

    public NotificationService() {
        // Pretplacujemo se na stream notifikacija
        notificationSubject
                .filter(msg -> msg != null && !msg.isEmpty())
                .map(msg -> "[NOTIFICATION] " + msg)
                .subscribe(
                        msg -> {
                            notificationLog.add(msg);
                            log.info("Processed notification: {}", msg);
                        },
                        error -> log.error("Error processing notification: {}", error.getMessage())
                );
    }

    // Prima poruke iz RabbitMQ
    @RabbitListener(queues = "booking-notifications")
    public void receiveMessage(String message) {
        log.info("Received message from RabbitMQ: {}", message);
        notificationSubject.onNext(message);
    }

    public Observable<String> getNotificationStream() {
        return notificationSubject.hide();
    }

    public List<String> getAllNotifications() {
        log.info("Fetching all notifications, count: {}", notificationLog.size());
        return new ArrayList<>(notificationLog);
    }

    public void sendNotification(String message) {
        log.info("Sending manual notification: {}", message);
        notificationSubject.onNext(message);
    }
}