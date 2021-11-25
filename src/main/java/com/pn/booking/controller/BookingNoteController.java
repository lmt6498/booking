package com.pn.booking.controller;

import com.pn.booking.model.dto.RestResult;
import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.request.BookingNoteRequest;
import com.pn.booking.model.dto.request.filter.ItemFilterRequest;
import com.pn.booking.model.dto.response.BookingItemResponse;
import com.pn.booking.model.dto.response.BookingNoteResponse;
import com.pn.booking.model.dto.response.BookingResponse;
import com.pn.booking.model.dto.response.FilterResponse;
import com.pn.booking.model.entity.BookingNote;
import com.pn.booking.service.BookingItemService;
import com.pn.booking.service.BookingNoteService;
import com.pn.booking.service.BookingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings/{bookingId}/note")
public class BookingNoteController extends BaseController {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private BookingNoteService bookingNoteService;

  @GetMapping
  public ResponseEntity<RestResult<List<BookingNote>>> getBookingNotes(
      @PathVariable Long bookingId
  ) {
    List<BookingNote> result = bookingNoteService.getBookingNotes(bookingId);
    return new ResponseEntity<>(
        new RestResult<List<BookingNote>>()
            .message("Request success")
            .data(result),
        HttpStatus.OK
    );
  }

  @PostMapping
  public ResponseEntity<RestResult<List<BookingNote>>> addNoteToBooking(
      @PathVariable Long bookingId,
      @RequestBody List<BookingNoteRequest> bookingNotes
  ) {
    return new ResponseEntity<>(
        new RestResult<List<BookingNote>>()
            .status(RestResult.STATUS_SUCCESS)
            .data(bookingNoteService.createOrUpdate(bookingNotes))
            .message("Add notes to booking success"),
        HttpStatus.OK
    );
  }

  @DeleteMapping("/{bookingNoteId}")
  public ResponseEntity<RestResult> removeNoteFromBooking(
      @PathVariable Long bookingId,
      @PathVariable Long bookingItemId
  ) {
    return new ResponseEntity<>(
        new RestResult<>()
            .status(RestResult.STATUS_SUCCESS)
            .message("Remove notes from booking success"),
        HttpStatus.OK
    );
  }

}
