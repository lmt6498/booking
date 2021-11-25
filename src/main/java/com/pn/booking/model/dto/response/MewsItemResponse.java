package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsItemResponse {

  public enum State {

    OPEN,
    CLOSED,
    INACTIVE,
    CANCELED;

    @JsonValue
    public String getValue() {
      switch (this) {
        case OPEN:
          return "Open";
        case CLOSED:
          return "Closed";
        case INACTIVE:
          return "Inactive";
        case CANCELED:
          return "Canceled";
      }
      return null;
    }
  }

  @NotNull
  @JsonProperty("Name")
  private String name;

  @NotNull
  @JsonProperty("State")
  private State state;

  @JsonProperty("OrderId")
  private String orderId;

  @JsonProperty("ProductId")
  private String productId;

  @JsonProperty("ServiceId")
  private String serviceId;

  @JsonProperty("ConsumptionUtc")
  private String consumptionUtc;

  @JsonProperty("Amount")
  private MewsAmountValueResponse amount;

  @JsonProperty("Type")
  private String type;
}
