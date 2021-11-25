package com.pn.booking.model.jpa.spec.director;

import com.pn.booking.common.utils.CommonUtils;
import com.pn.booking.model.dto.request.filter.BookingFilterRequest;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;
import com.pn.booking.model.jpa.spec.builder.BookingFilterSpecBuilderImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingFilterSpecDirector {
  private static final Logger logger = LoggerFactory.getLogger(BookingFilterSpecDirector.class);

  private final BookingFilterRequest filter;

  public BookingFilterSpecDirector(BookingFilterRequest filter) {
    this.filter = filter;
  }

  public FilterSpecBuilder<Booking> buildSimpleFilter() {
    logger.error("filter start and end time:");
    logger.error(filter.getStartDateTime());
    logger.error(filter.getEndDateTime());
    
    return new BookingFilterSpecBuilderImpl(filter)
        .buildBusinessId(filter.getBusinessId());
        // .buildStartTime(CommonUtils.convertStringToTimestampUTC(filter.getStartDateTime()))
        // .buildEndTime(CommonUtils.convertStringToTimestampUTC(filter.getEndDateTime()));
        // .buildStatusList(filter.getBookingStatus())
  }

}
