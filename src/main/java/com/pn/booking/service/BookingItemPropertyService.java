package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.request.BookingItemPropertyRequest;
import com.pn.booking.model.entity.BookingItemProperty;

public interface BookingItemPropertyService {
  public List<BookingItemProperty> createOrUpdate(List<BookingItemPropertyRequest> request);
  public void deleteProperties(List<Long> bookingItemPropertyIds);
}
