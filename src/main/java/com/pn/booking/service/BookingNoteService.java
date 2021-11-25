package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.request.BookingNoteRequest;
import com.pn.booking.model.entity.BookingNote;

public interface BookingNoteService {
  
  void removeAllByBookingId(Long bookingId);
  public List<BookingNote> createOrUpdate(List<BookingNoteRequest> request);
  public List<BookingNote> getBookingNotes(Long bookingId);
}
