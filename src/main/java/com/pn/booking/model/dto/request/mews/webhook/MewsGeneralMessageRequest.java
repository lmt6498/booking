package com.pn.booking.model.dto.request.mews.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MewsGeneralMessageRequest {

  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class MewsEntityRequest {

    @JsonProperty("ServiceOrders")
    private List<MewsReservationRequest> serviceOrders;
  }

  @NotNull
  @JsonProperty("EnterpriseId")
  private String enterpriseId;

  @NotNull
  @JsonProperty("IntegrationId")
  private String integrationId;

  @NotNull
  @JsonProperty("Events")
  private List<MewsEventRequest> events;

  @NotNull
  @JsonProperty("Entities")
  private MewsEntityRequest entities;
}
