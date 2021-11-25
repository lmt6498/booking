package com.pn.booking.model.dto.request.mews;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MewsReservationSearchRequest extends MewsRequest {

  @JsonProperty("ReservationIds")
  private List<String> reservationIds;

  @JsonProperty("Numbers")
  private List<String> numbers;

  @JsonProperty("Extent")
  private Map<String, Boolean> extent;
}
