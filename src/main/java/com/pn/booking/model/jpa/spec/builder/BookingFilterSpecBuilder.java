package com.pn.booking.model.jpa.spec.builder;

import java.sql.Timestamp;
import java.util.List;

import com.pn.booking.model.constant.BookingStatus;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;

public interface BookingFilterSpecBuilder extends FilterSpecBuilder<Booking> {

  BookingFilterSpecBuilder buildIdentifier(String identifier);

  BookingFilterSpecBuilder buildBusinessId(Long businessId);

  BookingFilterSpecBuilder buildStartTime(Timestamp startDateTime);

  BookingFilterSpecBuilder buildEndTime(Timestamp endDateTime);

  BookingFilterSpecBuilder buildStatusList(List<BookingStatus> status);

}
