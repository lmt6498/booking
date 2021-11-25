package com.pn.booking.repository.custom;

import java.util.List;

import com.pn.booking.model.dto.request.filter.BookingFilterRequest;
import com.pn.booking.model.entity.Booking;

public interface BookingRepositoryCustom {
  
  public List<Booking> search(BookingFilterRequest request);
  public Long count(BookingFilterRequest request);
  public Long countToBeCheckedIn(BookingFilterRequest request);
  public Long countToBeCheckedOut(BookingFilterRequest request);
  
}
