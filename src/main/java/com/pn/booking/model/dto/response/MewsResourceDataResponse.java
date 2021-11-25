package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsResourceDataResponse {

  @JsonProperty("Discriminator")
  private String discriminator;

  @JsonProperty("Value")
  private MewsResourceDataValueResponse value;
}