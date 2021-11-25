package com.pn.booking.model.dto.request.mews;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MewsUpdateReservationRequest extends MewsRequest {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Value {

    @JsonProperty("Value")
    private Object value;
  }

  @Getter
  @Setter
  @Builder
  public static class Detail {

    @JsonProperty("ReservationId")
    private String reservationId;

    @JsonProperty("StartUtc")
    private Value startUtc;

    @JsonProperty("EndUtc")
    private Value endUtc;

    @JsonProperty("AdultCount")
    private Value adultCount;

    @JsonProperty("ChildCount")
    private Value childCount;
  }

  @JsonProperty("ReservationUpdates")
  private List<Detail> reservationUpdates;
}
