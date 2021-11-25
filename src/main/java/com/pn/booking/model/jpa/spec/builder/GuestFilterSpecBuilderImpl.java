package com.pn.booking.model.jpa.spec.builder;

import com.pn.booking.model.dto.request.filter.FilterRequest;
import com.pn.booking.model.entity.Customer;

public class GuestFilterSpecBuilderImpl extends AbstractSpecBuilder<Customer> implements GuestFilterSpecBuilder {

  public GuestFilterSpecBuilderImpl(FilterRequest filter) {
    super(filter, null);
  }

  @Override
  public GuestFilterSpecBuilder buildBookingId(Long bookingId) {
    if (bookingId != null) {
      specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bookingId"), bookingId));
    }
    return this;
  }
}
