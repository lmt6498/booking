package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsReservationResourceResponse {
  
  @JsonProperty("Id")
  private String id;

  @JsonProperty("IsActive")
  private String isActive;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("State")
  private String state;

}
