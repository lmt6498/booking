package com.pn.booking.repository;

import com.pn.booking.model.entity.BookingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookingItemRepository extends JpaRepository<BookingItem, Long>, JpaSpecificationExecutor<BookingItem> {
  List<BookingItem> findByBookingIdIn(Set<Long> bookingIds);

  List<BookingItem> findByBookingId(Long bookingId);

  Optional<BookingItem> findBookingItemByExternalId(String externalId);
  BookingItem findFirstById(Long bookingItemId);
}
