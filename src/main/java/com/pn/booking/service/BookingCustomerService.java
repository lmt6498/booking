package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.Customer;

public interface BookingCustomerService {
  public void linkCustomersToBookings(Booking booking, List<Customer> customers, String ownerId, String bookerId);
}
