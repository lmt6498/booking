package com.pn.booking.model.dto.request.mews.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsIntegrationMessageRequest {

  @JsonProperty("Action")
  private String action;

  @JsonProperty("Data")
  private MewsIntegrationDataRequest data;
}
