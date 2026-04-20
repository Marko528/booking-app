package com.booking.userservice.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserServiceE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void testHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/api/users/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("UP"));
    }

    @Test
    void testCreateAndGetUser() {
        String userJson = """
                {
                    "firstName": "Test",
                    "lastName": "User",
                    "email": "e2e@test.com",
                    "password": "password123",
                    "role": "GUEST"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(userJson, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                getBaseUrl() + "/api/users", request, String.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                getBaseUrl() + "/api/users", String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    void testActuatorHealth() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/actuator/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("UP"));
    }
}