package com.pn.booking.model.dto.request.mews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MewsReservationRequest {

  public enum State {
    ENQUIRED,
    REQUESTED,
    OPTIONAL,
    CONFIRMED,
    STARTED,
    PROCESSED,
    CANCELED;

    @JsonValue
    public String getValue() {
      switch (this) {
        case ENQUIRED:
          return "Enquired";
        case REQUESTED:
          return "Requested";
        case OPTIONAL:
          return "Optional";
        case CONFIRMED:
          return "Confirmed";
        case STARTED:
          return "Started";
        case PROCESSED:
          return "Processed";
        case CANCELED:
          return "Canceled";
      }
      return null;
    }

    public static State getState(String value) {
      for (State state: values()) {
        if (state.name().equals(value)) {
          return state;
        }
      }
      return null;
    }
  }

  @NotNull
  @JsonProperty("Id")
  private String id;

  @NotNull
  @JsonProperty("ServiceId")
  private String serviceId;

  @JsonProperty("CreatedUtc")
  private String createdUtc;

  @JsonProperty("CancelledUtc")
  private String cancelledUtc;

  @NotNull
  @JsonProperty("StartUtc")
  private String startUtc;

  @NotNull
  @JsonProperty("EndUtc")
  private String endUtc;

  @NotNull
  @JsonProperty("AdultCount")
  private Integer adultCount;

  @NotNull
  @JsonProperty("ChildCount")
  private Integer childCount;

  @NotNull
  @JsonProperty("CustomerId")
  private String customerId;

  @JsonProperty("CompanyId")
  private String companyId;

  @NotNull
  @JsonProperty("AssignedResourceId")
  private String assignedResourceId;

  @NotNull
  @JsonProperty("BookerId")
  private String bookerId;

  @NotNull
  @JsonProperty("State")
  private State state;

  @NotNull
  @JsonProperty("Number")
  private String number;

  @JsonProperty("CompanionIds")
  private List<String> companionIds;

  @JsonProperty("BusinessSegmentIds")
  private List<String> businessSegmentIds;
}
