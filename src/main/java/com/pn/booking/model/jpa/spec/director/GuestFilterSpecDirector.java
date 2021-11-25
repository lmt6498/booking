package com.pn.booking.model.jpa.spec.director;

import com.pn.booking.model.dto.request.filter.GuestFilterRequest;
import com.pn.booking.model.entity.Customer;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;
import com.pn.booking.model.jpa.spec.builder.GuestFilterSpecBuilderImpl;

public class GuestFilterSpecDirector {

  private final GuestFilterRequest filter;

  public GuestFilterSpecDirector(GuestFilterRequest filter) {
    this.filter = filter;
  }

  public FilterSpecBuilder<Customer> build() {
    return new GuestFilterSpecBuilderImpl(filter)
        .buildBookingId(filter.getBookingId());
  }
}
