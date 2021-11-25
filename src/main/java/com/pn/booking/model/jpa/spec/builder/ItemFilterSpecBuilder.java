package com.pn.booking.model.jpa.spec.builder;

import com.pn.booking.model.entity.BookingItem;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;

public interface ItemFilterSpecBuilder extends FilterSpecBuilder<BookingItem> {

  ItemFilterSpecBuilder buildBookingId(Long bookingId);

}
