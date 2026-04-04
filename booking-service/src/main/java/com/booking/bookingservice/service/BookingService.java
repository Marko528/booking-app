package com.booking.bookingservice.service;

import com.booking.bookingservice.config.RabbitMQConfig;
import com.booking.bookingservice.model.Booking;
import com.booking.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RabbitTemplate rabbitTemplate;

    public Booking createBooking(Booking booking) {
        log.info("Creating booking for user: {} property: {}",
                booking.getUserId(), booking.getPropertyId());
        booking.setStatus("PENDING");
        Booking saved = bookingRepository.save(booking);

        // Salji poruku notification-service-u preko RabbitMQ
        String message = "Booking created for user " + booking.getUserId()
                + " property " + booking.getPropertyId();
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
        log.info("Sent notification message to RabbitMQ: {}", message);

        return saved;
    }

    public List<Booking> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        log.info("Fetching booking with id: {}", id);
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByUser(Long userId) {
        log.info("Fetching bookings for user: {}", userId);
        return bookingRepository.findByUserId(userId);
    }

    public Booking updateStatus(Long id, String status) {
        log.info("Updating booking {} status to {}", id, status);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        log.info("Deleting booking with id: {}", id);
        bookingRepository.deleteById(id);
    }
}