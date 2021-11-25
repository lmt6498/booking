package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.request.mews.MewsReservationRequest;
import com.pn.booking.model.dto.response.BookingItemResponse;
import com.pn.booking.model.dto.response.MewsFetchingReservationResponse;
import com.pn.booking.model.entity.Booking;

public interface MewsBookingItemService {
  
  List<BookingItemResponse> saveBookingItems(Booking booking, MewsReservationRequest reservation, MewsFetchingReservationResponse mewsResponse);
  
}
