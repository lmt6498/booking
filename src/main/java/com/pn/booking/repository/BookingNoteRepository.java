package com.pn.booking.repository;

import com.pn.booking.model.entity.BookingNote;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingNoteRepository extends JpaRepository<BookingNote, Long>, JpaSpecificationExecutor<BookingNote> {

  List<BookingNote> findByBookingIdIn(Set<Long> bookingIds);
  List<BookingNote> findByBookingId(Long bookingId);
  BookingNote findFirstBySourceAndExternalId(String source, String externalId);
  
}
