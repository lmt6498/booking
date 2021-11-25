package com.pn.booking.model.jpa.spec.builder;

import com.pn.booking.model.entity.Customer;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;

public interface GuestFilterSpecBuilder extends FilterSpecBuilder<Customer> {

  GuestFilterSpecBuilder buildBookingId(Long bookingId);

}
