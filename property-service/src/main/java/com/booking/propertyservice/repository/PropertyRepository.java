package com.booking.propertyservice.repository;

import com.booking.propertyservice.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByAvailableTrue();
    List<Property> findByHostId(Long hostId);
}