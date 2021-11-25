package com.pn.booking.model.dto.request.mews;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MewsRequest {

  @JsonProperty("ClientToken")
  private String clientToken;

  @JsonProperty("AccessToken")
  private String accessToken;

  @JsonProperty("Client")
  private String client = "PouchCONNECT 1.0.0";
}
