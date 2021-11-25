package com.pn.booking.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.BookingCustomer;
import com.pn.booking.model.entity.Customer;
import com.pn.booking.repository.BookingCustomerRepository;
import com.pn.booking.service.BookingCustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingCustomerServiceImpl implements BookingCustomerService {

  @Autowired
  private BookingCustomerRepository bookingCustomerRepository;
  
  @Override
  public void linkCustomersToBookings(Booking booking, List<Customer> customers, String ownerId, String bookerId) {
    List<BookingCustomer> initialBookingCustomers = bookingCustomerRepository.findByBookingId(booking.getId());
    bookingCustomerRepository.deleteAll(initialBookingCustomers);

    List<BookingCustomer> bookingCustomers = new ArrayList<>();
    for (Customer customer : customers) {
      BookingCustomer bookingCustomer = new BookingCustomer();
      bookingCustomer.setBookingId(booking.getId());
      bookingCustomer.setIsBooker(customer.getExternalId().equals(bookerId));
      bookingCustomer.setIsOwner(customer.getExternalId().equals(ownerId));
      bookingCustomer.setCustomerId(customer.getId());
      bookingCustomers.add(bookingCustomer);
    }

    bookingCustomerRepository.saveAll(bookingCustomers);
  }

}
