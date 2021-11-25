package com.pn.booking.model.dto.request.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
public class ItemFilterRequest extends FilterRequest {

  private Long bookingId;
}
