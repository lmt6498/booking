package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsReservationResponse {

  @JsonProperty("Id")
  private String id;

  @JsonProperty("CustomerId")
  private String customerId;
}
