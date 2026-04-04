package com.booking.bookingservice;

import com.booking.bookingservice.model.Booking;
import com.booking.bookingservice.repository.BookingRepository;
import com.booking.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private BookingService bookingService;

    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setUserId(1L);
        testBooking.setPropertyId(1L);
        testBooking.setCheckIn(LocalDate.now());
        testBooking.setCheckOut(LocalDate.now().plusDays(3));
        testBooking.setTotalPrice(300.0);
        testBooking.setStatus("PENDING");
    }

    @Test
    void testCreateBooking() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        Booking created = bookingService.createBooking(testBooking);
        assertNotNull(created);
        assertEquals("PENDING", created.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(rabbitTemplate, times(1)).convertAndSend(
                anyString(), anyString(), anyString()
        );
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(testBooking));
        List<Booking> bookings = bookingService.getAllBookings();
        assertEquals(1, bookings.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetBookingById() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        Optional<Booking> found = bookingService.getBookingById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getUserId());
    }

    @Test
    void testUpdateStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        Booking updated = bookingService.updateStatus(1L, "CONFIRMED");
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testDeleteBooking() {
        doNothing().when(bookingRepository).deleteById(1L);
        bookingService.deleteBooking(1L);
        verify(bookingRepository, times(1)).deleteById(1L);
    }
}