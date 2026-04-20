package com.booking.propertyservice.e2e;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@Disabled("E2E tests require running database")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PropertyServiceE2ETest {

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
                getBaseUrl() + "/api/properties/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("UP"));
    }

    @Test
    void testCreateAndGetProperty() {
        String propertyJson = """
                {
                    "name": "Test Property",
                    "location": "Novi Sad",
                    "description": "Test opis",
                    "pricePerNight": 100.0,
                    "maxGuests": 4,
                    "available": true,
                    "hostId": 1
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(propertyJson, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                getBaseUrl() + "/api/properties", request, String.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                getBaseUrl() + "/api/properties", String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    void testGetAvailableProperties() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/api/properties/available", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testActuatorHealth() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/actuator/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}