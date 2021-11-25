package com.pn.booking.model.dto.request.filter;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
public class BookingFilterRequest extends FilterRequest {

  private Long businessId;
  private String startDateTime;
  private String endDateTime;
  private String source;
  private List<String> bookingStatus;
  private List<String> countStatus;
  private String sort;
}
