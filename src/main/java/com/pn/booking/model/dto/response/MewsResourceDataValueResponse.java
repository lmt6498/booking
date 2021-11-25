package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsResourceDataValueResponse {
  @JsonProperty("FloorNumber")
  private String floorNumber;

  @JsonProperty("LocationNotes")
  private String locationNotes;
}
