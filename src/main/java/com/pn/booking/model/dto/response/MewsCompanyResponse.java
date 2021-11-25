package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsCompanyResponse {
  
  @JsonProperty("Id")
  private String id;

  @JsonProperty("Name")
  private String name;
  
}
