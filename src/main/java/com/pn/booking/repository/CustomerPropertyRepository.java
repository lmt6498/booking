package com.pn.booking.repository;

import com.pn.booking.model.entity.CustomerProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerPropertyRepository extends JpaRepository<CustomerProperty, Long>, JpaSpecificationExecutor<CustomerProperty> {

    CustomerProperty findFirstBySourceAndExternalIdAndBookingId(String source, String externalId, Long bookingId);
    
}
