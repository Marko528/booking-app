package com.booking.userservice;

import com.booking.userservice.model.User;
import com.booking.userservice.repository.UserRepository;
import com.booking.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Marko");
        testUser.setLastName("Markovic");
        testUser.setEmail("marko@example.com");
        testUser.setPassword("password123");
        testUser.setRole("GUEST");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        User created = userService.createUser(testUser);
        assertNotNull(created);
        assertEquals("Marko", created.getFirstName());
        assertEquals("marko@example.com", created.getEmail());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));
        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Optional<User> found = userService.getUserById(1L);
        assertTrue(found.isPresent());
        assertEquals("Marko", found.get().getFirstName());
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("marko@example.com"))
                .thenReturn(Optional.of(testUser));
        Optional<User> found = userService.getUserByEmail("marko@example.com");
        assertTrue(found.isPresent());
        assertEquals("marko@example.com", found.get().getEmail());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<User> found = userService.getUserById(99L);
        assertFalse(found.isPresent());
    }
}