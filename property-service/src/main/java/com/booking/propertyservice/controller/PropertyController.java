package com.booking.propertyservice.controller;

import com.booking.propertyservice.model.Property;
import com.booking.propertyservice.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Slf4j
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        log.info("POST /api/properties - creating property");
        Property created = propertyService.createProperty(property);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        log.info("GET /api/properties - fetching all");
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Property>> getAvailableProperties() {
        log.info("GET /api/properties/available");
        return ResponseEntity.ok(propertyService.getAvailableProperties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.getPropertyById(id);
        if (property.isPresent()) {
            return ResponseEntity.ok(property.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<Property>> getPropertiesByHost(@PathVariable Long hostId) {
        return ResponseEntity.ok(propertyService.getPropertiesByHost(hostId));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Property> updateAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) {
        Property updated = propertyService.updateAvailability(id, available);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        log.info("DELETE /api/properties/{}", id);
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Property Service is UP");
    }
}