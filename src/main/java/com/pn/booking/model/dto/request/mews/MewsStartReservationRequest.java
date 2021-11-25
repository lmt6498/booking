package com.pn.booking.model.dto.request.mews;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MewsStartReservationRequest extends MewsRequest {

  @JsonProperty("ReservationId")
  private String reservationId;
}
