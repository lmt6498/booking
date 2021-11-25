package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.response.MewsNotesResponse;
import com.pn.booking.model.entity.BookingNote;

public interface MewsBookingNoteService {
  
  public List<BookingNote> saveNotes(List<MewsNotesResponse> notes);
      
}
