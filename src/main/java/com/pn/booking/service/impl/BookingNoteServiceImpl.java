package com.pn.booking.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.pn.booking.model.dto.mapper.BookingNoteMapper;
import com.pn.booking.model.dto.request.BookingNoteRequest;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.BookingNote;
import com.pn.booking.repository.BookingNoteRepository;
import com.pn.booking.service.BookingNoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class BookingNoteServiceImpl extends ContextService implements BookingNoteService {

  @Autowired
  private BookingNoteRepository bookingNoteRepository;

  @Transactional
  @Override
  public List<BookingNote> createOrUpdate(List<BookingNoteRequest> request) {
    List<BookingNote> bookingNotes = BookingNoteMapper.INSTANCE.toEntities(request);
    List<BookingNote> bookingNotesForSaving = new ArrayList<BookingNote>();
    if (!CollectionUtils.isEmpty(bookingNotes)) {
      for (BookingNote bookingNote : bookingNotes) {
        BookingNote bookingNoteEntity = bookingNoteRepository.findFirstBySourceAndExternalId(bookingNote.getSource(), bookingNote.getExternalId());
        if (bookingNoteEntity != null) {
          bookingNote.setId(bookingNoteEntity.getId());
        }
        bookingNotesForSaving.add(bookingNote);
      }
      bookingNotesForSaving = bookingNoteRepository.saveAll(bookingNotesForSaving);
    }
    return bookingNotesForSaving;
  }

  @Transactional
  @Override
  public void removeAllByBookingId(Long bookingId) {
    List<BookingNote> items = bookingNoteRepository.findByBookingId(bookingId);
    bookingNoteRepository.deleteAll(items);
  }

  @Override
  public List<BookingNote> getBookingNotes(Long bookingId) {
    return bookingNoteRepository.findByBookingId(bookingId);
  }
  
}
