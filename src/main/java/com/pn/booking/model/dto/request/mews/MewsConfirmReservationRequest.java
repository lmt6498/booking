package com.pn.booking.model.dto.request.mews;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MewsConfirmReservationRequest extends MewsRequest {

  @JsonProperty("ReservationIds")
  private List<String> reservationIds;
}
