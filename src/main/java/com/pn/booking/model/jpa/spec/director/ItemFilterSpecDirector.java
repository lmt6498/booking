package com.pn.booking.model.jpa.spec.director;

import com.pn.booking.model.dto.request.filter.ItemFilterRequest;
import com.pn.booking.model.entity.BookingItem;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;
import com.pn.booking.model.jpa.spec.builder.ItemFilterSpecBuilderImpl;

public class ItemFilterSpecDirector {

  private final ItemFilterRequest filter;

  public ItemFilterSpecDirector(ItemFilterRequest filter) {
    this.filter = filter;
  }

  public FilterSpecBuilder<BookingItem> build() {
    return new ItemFilterSpecBuilderImpl(filter)
        .buildBookingId(filter.getBookingId());
  }
}
