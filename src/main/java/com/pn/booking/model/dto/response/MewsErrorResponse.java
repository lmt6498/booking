package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsErrorResponse {

  @JsonProperty("Message")
  private String message;

  @JsonProperty("Details")
  private String details;
}
