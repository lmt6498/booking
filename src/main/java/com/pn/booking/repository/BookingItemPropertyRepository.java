package com.pn.booking.repository;

import java.util.List;

import com.pn.booking.model.entity.BookingItemProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingItemPropertyRepository extends JpaRepository<BookingItemProperty, Long>, JpaSpecificationExecutor<BookingItemProperty> {

  BookingItemProperty findFirstBySourceAndExternalIdAndBookingItemId(String source, String externalId, Long bookingItemId);

  void deleteAllByIdIn(List<Long> bookingItemPropertyIds);
  
}
