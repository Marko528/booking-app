package com.booking.propertyservice.service;

import com.booking.propertyservice.model.Property;
import com.booking.propertyservice.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public Property createProperty(Property property) {
        log.info("Creating new property: {}", property.getName());
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        log.info("Fetching all properties");
        return propertyRepository.findAll();
    }

    public List<Property> getAvailableProperties() {
        log.info("Fetching available properties");
        return propertyRepository.findByAvailableTrue();
    }

    public Optional<Property> getPropertyById(Long id) {
        log.info("Fetching property with id: {}", id);
        return propertyRepository.findById(id);
    }

    public List<Property> getPropertiesByHost(Long hostId) {
        log.info("Fetching properties for host: {}", hostId);
        return propertyRepository.findByHostId(hostId);
    }

    public Property updateAvailability(Long id, Boolean available) {
        log.info("Updating availability for property: {}", id);
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        property.setAvailable(available);
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        log.info("Deleting property with id: {}", id);
        propertyRepository.deleteById(id);
    }
}