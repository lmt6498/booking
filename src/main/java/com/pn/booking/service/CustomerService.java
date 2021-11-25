package com.pn.booking.service;

import com.pn.booking.model.dto.request.CustomerRequest;
import com.pn.booking.model.dto.request.filter.GuestFilterRequest;
import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.dto.response.FilterResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.Customer;

import java.util.List;

public interface CustomerService {

  CustomerResponse get(Long customerId);

  List<CustomerResponse> create(Booking booking, List<CustomerRequest> requestList);

  List<Customer> createOrUpdate(List<CustomerRequest> requestList);

  CustomerResponse update(Long bookingCustomerId, CustomerRequest request);

  FilterResponse<CustomerResponse> filter(GuestFilterRequest filter);

  CustomerResponse remove(Long customerId);

  void removeInBooking(Long bookingId, List<Long> customerIds);
}
