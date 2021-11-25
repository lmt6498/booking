package com.pn.booking.model.jpa.spec.builder;

import com.pn.booking.model.constant.BookingStatus;
import com.pn.booking.model.dto.request.filter.FilterRequest;
import com.pn.booking.model.entity.Booking;
import java.sql.Timestamp;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class BookingFilterSpecBuilderImpl extends AbstractSpecBuilder<Booking> implements BookingFilterSpecBuilder {

  public BookingFilterSpecBuilderImpl() {
    super();
  }

  public BookingFilterSpecBuilderImpl(FilterRequest filter) {
    super(filter, null);
  }

  public BookingFilterSpecBuilderImpl(FilterRequest filter, List<Specification<Booking>> specs) {
    super(filter, specs);
  }

  @Override
  public BookingFilterSpecBuilder buildIdentifier(String identifier) {
    if (StringUtils.isNotBlank(identifier)) {
      specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("identifier"), identifier));
    }
    return this;
  }

  @Override
  public BookingFilterSpecBuilder buildBusinessId(Long businessId) {
    if (businessId != null) {
      specs.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("businessId"), businessId));
    }
    return this;
  }

  @Override
  public BookingFilterSpecBuilder buildStartTime(Timestamp startDateTime) {
    if (startDateTime != null) {
      specs.add((root, query, criteriaBuilder) ->
        criteriaBuilder.lessThan(
          root.get("startTime"),
          startDateTime
        )
      );
    }
    return this;
  }

  @Override
  public BookingFilterSpecBuilder buildEndTime(Timestamp endDateTime) {
    if (endDateTime != null) {
      specs.add((root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThan(
          root.get("endTime"),
          endDateTime
        )
      );
    }
    return this;
  }

  @Override
  public BookingFilterSpecBuilder buildStatusList(List<BookingStatus> status) {
    // TODO Auto-generated method stub
    return null;
  }

}
