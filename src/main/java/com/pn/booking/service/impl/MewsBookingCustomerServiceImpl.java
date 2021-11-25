package com.pn.booking.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.pn.booking.model.dto.mapper.CustomerMapper;
import com.pn.booking.model.dto.request.CustomerRequest;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest;
import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.dto.response.MewsCustomerResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.Customer;
import com.pn.booking.model.entity.Booking.Source;
import com.pn.booking.service.BookingCustomerService;
import com.pn.booking.service.CustomerService;
import com.pn.booking.service.MewsBookingCustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MewsBookingCustomerServiceImpl implements MewsBookingCustomerService {
  
  @Autowired
  private CustomerService customerService;

  @Autowired
  private BookingCustomerService bookingCustomerService;

  @Override
  public List<CustomerResponse> saveBookingCustomers(Booking booking, MewsReservationRequest reservation, List<MewsCustomerResponse> request) {
    
    List<CustomerRequest> customers = new ArrayList<>();
    for (MewsCustomerResponse customer : request) {
      if ((reservation.getCompanionIds() != null && reservation.getCompanionIds().contains(customer.getId())) || 
          customer.getId().equals(reservation.getCustomerId()) ||
          customer.getId().equals(reservation.getBookerId())) {
          customers.add(convertToCustomer(customer, reservation.getCustomerId(), reservation.getBookerId()));
      }
    }
    List<Customer> savedCustomers = customerService.createOrUpdate(customers);
    bookingCustomerService.linkCustomersToBookings(booking, savedCustomers, reservation.getCustomerId(), reservation.getBookerId());

    return CustomerMapper.INSTANCE.toResponses(savedCustomers);
  }

  private CustomerRequest convertToCustomer(MewsCustomerResponse mewsCustomerResponse, String mainCustomerId, String bookerCustomerId) {
    return CustomerRequest.builder()
        .email(mewsCustomerResponse.getEmail())
        .firstName(mewsCustomerResponse.getFirstName())
        .lastName(mewsCustomerResponse.getLastName())
        .mobileNumber(mewsCustomerResponse.getPhone())
        .externalId(mewsCustomerResponse.getId())
        .source(Source.MEWS.name())
        .birthdate(mewsCustomerResponse.getBirthdate())
        .gender(normalizeGender(mewsCustomerResponse.getSex()))
        .build();
  }

  private String normalizeGender(String gender) {
    if (gender != null) {
      if (gender.toLowerCase().indexOf("f") > -1) {
        return "F";
      } else if (gender.toLowerCase().indexOf("m") > -1)  {
        return "M";
      } else {
        return "O";
      }
    }
    return null;
  }

}
