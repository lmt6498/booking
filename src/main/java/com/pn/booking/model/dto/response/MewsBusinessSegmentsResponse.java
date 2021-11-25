package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsBusinessSegmentsResponse {
  
  @JsonProperty("Id")
  private String id;

  @JsonProperty("ServiceId")
  private String serviceId;

  @JsonProperty("Name")
  private String name;
}
