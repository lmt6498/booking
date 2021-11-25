package com.pn.booking.service;

import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.request.filter.ItemFilterRequest;
import com.pn.booking.model.dto.response.BookingItemResponse;
import com.pn.booking.model.dto.response.FilterResponse;
import com.pn.booking.model.entity.Booking;
import java.util.List;
import java.util.Map;

public interface BookingItemService {

  BookingItemResponse get(Long bookingItemId);

  List<BookingItemResponse> create(Booking booking, List<BookingItemRequest> bookingItems);

  BookingItemResponse update(Long bookingId, BookingItemRequest request);

  FilterResponse<BookingItemResponse> filter(ItemFilterRequest filter);

  BookingItemResponse remove(Long bookingId, Long bookingItemId);

  void removeAllByBookingId(Long bookingId);

  Map<Long, List<BookingItemResponse>> forceCreate(Map<Booking, List<BookingItemRequest>> requestMap);

  List<BookingItemResponse> addItemToBooking(Long bookingId, List<BookingItemRequest> bookingItems);
}
