package com.pn.booking.model.jpa.spec.builder;

import com.pn.booking.model.dto.request.filter.FilterRequest;
import com.pn.booking.model.entity.BookingItem;

public class ItemFilterSpecBuilderImpl extends AbstractSpecBuilder<BookingItem> implements ItemFilterSpecBuilder {

  public ItemFilterSpecBuilderImpl(FilterRequest filter) {
    super(filter, null);
  }

  @Override
  public ItemFilterSpecBuilder buildBookingId(Long bookingId) {
    if (bookingId != null) {
      specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bookingId"), bookingId));
    }
    return this;
  }
}
