package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.request.mews.MewsReservationRequest;
import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.dto.response.MewsCustomerResponse;
import com.pn.booking.model.entity.Booking;

public interface MewsBookingCustomerService {
  
  public List<CustomerResponse> saveBookingCustomers(Booking booking, MewsReservationRequest reservation, List<MewsCustomerResponse> customers);

}
