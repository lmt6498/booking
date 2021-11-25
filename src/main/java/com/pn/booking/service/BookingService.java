package com.pn.booking.service;

import com.pn.booking.model.dto.request.CustomerRequest;
import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.request.BookingRequest;
import com.pn.booking.model.dto.request.filter.BookingFilterRequest;
import com.pn.booking.model.dto.response.BookingResponse;
import com.pn.booking.model.entity.Booking;

import java.util.List;

public interface BookingService {

  public BookingResponse get(Long bookingId);

  public BookingResponse create(BookingRequest request);

  public BookingResponse update(Long bookingId, BookingRequest request);

  public BookingResponse checkIn(Long bookingId);

  public BookingResponse checkOut(Long bookingId);

  public List<BookingResponse> search(BookingFilterRequest filter);

  public Long count(BookingFilterRequest filter);

  public BookingResponse addGuests(Long bookingId, List<CustomerRequest> guests);

  public BookingResponse updateGuests(Long bookingId, List<CustomerRequest> guests);

  public BookingResponse addItems(Long bookingId, List<BookingItemRequest> items);

  public List<Booking> createOrUpdate(List<BookingRequest> requestList, String source);

  public Long countToBeCheckedIn(BookingFilterRequest request);

  public Long countToBeCheckedOut(BookingFilterRequest request);

}
