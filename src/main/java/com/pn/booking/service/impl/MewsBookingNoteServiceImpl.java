package com.pn.booking.service.impl;

import com.pn.booking.model.dto.request.BookingNoteRequest;
import com.pn.booking.model.dto.response.MewsNotesResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.Booking.Source;
import com.pn.booking.model.entity.BookingNote;
import com.pn.booking.repository.BookingRepository;
import com.pn.booking.service.BookingNoteService;
import com.pn.booking.service.MewsBookingNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MewsBookingNoteServiceImpl implements MewsBookingNoteService {
  
  @Autowired
  private BookingNoteService bookingNoteService;

  @Autowired
  private BookingRepository bookingRepository;

  @Override
  public List<BookingNote> saveNotes(List<MewsNotesResponse> notes) {
    List<BookingNoteRequest> bookingNotes = new ArrayList<BookingNoteRequest>();
    for (MewsNotesResponse note : notes) {
      Booking booking = bookingRepository.findFirstBySourceAndExternalId(Source.MEWS.name(), note.getOrderId()).get();
      if (booking != null) {
        bookingNotes.add(convertToBookingNote(note, booking));
      }
    }
    return bookingNoteService.createOrUpdate(bookingNotes);
  }

  private BookingNoteRequest convertToBookingNote(MewsNotesResponse response, Booking booking) {
    return BookingNoteRequest.builder()
      .bookingId(booking.getId())
      .externalId(response.getId())
      .source(Source.MEWS.name())
      .notes(response.getText())
      .build();
  }

}
