package com.pn.booking.controller;

import java.util.List;

import com.pn.booking.common.utils.CommonUtils;
import com.pn.booking.model.dto.RestResult;
import com.pn.booking.model.dto.request.BookingRequest;
import com.pn.booking.model.dto.request.filter.BookingFilterRequest;
import com.pn.booking.model.dto.response.BookingResponse;
import com.pn.booking.service.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bookings")
@Slf4j
public class BookingController extends BaseController {

  @Autowired
  private BookingService bookingService;

  @GetMapping("/{bookingId}")
  public ResponseEntity<RestResult<BookingResponse>> getByBookingId(
      @PathVariable Long bookingId
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .message("Request success")
            .data(bookingService.get(bookingId)),
        HttpStatus.OK
    );
  }

  @GetMapping
  public ResponseEntity<RestResult<List<BookingResponse>>> filter(
        @RequestParam(required = false, defaultValue = "20") Integer size,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "startTime ASC") String[] orders,
        @RequestParam(required = true) Long businessId,
        @RequestParam(required = false) String startDateTime,
        @RequestParam(required = false) String endDateTime,
        @RequestParam(required = false) List<String> bookingStatus,
        @RequestParam(required = false) String searchKey,
        @RequestParam(required = false, defaultValue = "ASC") String sort
  ) {
    
    if (startDateTime != null) {
        startDateTime = CommonUtils.urlDecode(startDateTime);
    }
    
    if (endDateTime != null) {
        endDateTime = CommonUtils.urlDecode(endDateTime);
    }

    if (searchKey != null) {
        searchKey = CommonUtils.urlDecode(searchKey);
    }
    
    BookingFilterRequest filter = BookingFilterRequest.builder()
        .page(page)
        .size(size)
        .orders(orders)
        .businessId(businessId)
        .startDateTime(startDateTime)
        .endDateTime(endDateTime)
        .bookingStatus(bookingStatus)
        .searchKey(searchKey)
        .sort(sort)
        .build();

    Long toBeCheckedIn = null;
    Long toBeCheckedOut = null;

    if (startDateTime != null && endDateTime != null) {
        toBeCheckedIn = bookingService.countToBeCheckedIn(filter);
        toBeCheckedOut = bookingService.countToBeCheckedOut(filter);
    }

    return new ResponseEntity<>(
        new RestResult<List<BookingResponse>>()
            .message("Request success")
            .data(bookingService.search(filter))
            .metadata("count", bookingService.count(filter))
            .metadata("toBeCheckedIn", toBeCheckedIn)
            .metadata("toBeCheckedOut", toBeCheckedOut),
        HttpStatus.OK
    );
  }

  @PostMapping
  public ResponseEntity<RestResult<BookingResponse>> createBooking(
      @RequestBody BookingRequest request
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .status(RestResult.STATUS_SUCCESS)
            .data(bookingService.create(request))
            .message("Create booking success"),
        HttpStatus.OK
    );
  }

  @PutMapping("/{bookingId}")
  public ResponseEntity<RestResult<BookingResponse>> updateBookingById(
      @PathVariable Long bookingId,
      @RequestBody BookingRequest request
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .message("Request success")
            .data(bookingService.update(bookingId, request)),
        HttpStatus.OK
    );
  }

  @PostMapping("/{bookingId}/check-in")
  public ResponseEntity<RestResult<BookingResponse>> checkInBooking(
      @PathVariable Long bookingId
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .message("Request success")
            .data(bookingService.checkIn(bookingId)),
        HttpStatus.OK
    );
  }

  @PostMapping("/{bookingId}/check-out")
  public ResponseEntity<RestResult<BookingResponse>> checkOutBooking(
      @PathVariable Long bookingId
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .message("Request success")
            .data(bookingService.checkOut(bookingId)),
        HttpStatus.OK
    );
  }
}
