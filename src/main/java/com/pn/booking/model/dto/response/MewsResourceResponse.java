package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsResourceResponse {

  @JsonProperty("Id")
  private String id;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Data")
  private MewsResourceDataResponse data;
  
}
