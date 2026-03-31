package com.booking.propertyservice;

import com.booking.propertyservice.model.Property;
import com.booking.propertyservice.repository.PropertyRepository;
import com.booking.propertyservice.service.PropertyService;
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
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    private Property testProperty;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setId(1L);
        testProperty.setName("Beach House");
        testProperty.setLocation("Novi Sad");
        testProperty.setDescription("Lepa kuca");
        testProperty.setPricePerNight(100.0);
        testProperty.setMaxGuests(4);
        testProperty.setAvailable(true);
        testProperty.setHostId(1L);
    }

    @Test
    void testCreateProperty() {
        when(propertyRepository.save(testProperty)).thenReturn(testProperty);
        Property created = propertyService.createProperty(testProperty);
        assertNotNull(created);
        assertEquals("Beach House", created.getName());
        verify(propertyRepository, times(1)).save(testProperty);
    }

    @Test
    void testGetAllProperties() {
        when(propertyRepository.findAll()).thenReturn(Arrays.asList(testProperty));
        List<Property> properties = propertyService.getAllProperties();
        assertEquals(1, properties.size());
        verify(propertyRepository, times(1)).findAll();
    }

    @Test
    void testGetPropertyById() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        Optional<Property> found = propertyService.getPropertyById(1L);
        assertTrue(found.isPresent());
        assertEquals("Beach House", found.get().getName());
    }

    @Test
    void testGetAvailableProperties() {
        when(propertyRepository.findByAvailableTrue()).thenReturn(Arrays.asList(testProperty));
        List<Property> available = propertyService.getAvailableProperties();
        assertEquals(1, available.size());
        assertTrue(available.get(0).getAvailable());
    }

    @Test
    void testDeleteProperty() {
        doNothing().when(propertyRepository).deleteById(1L);
        propertyService.deleteProperty(1L);
        verify(propertyRepository, times(1)).deleteById(1L);
    }
}