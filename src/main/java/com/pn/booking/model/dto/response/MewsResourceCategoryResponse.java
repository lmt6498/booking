package com.pn.booking.model.dto.response;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsResourceCategoryResponse {
  
  @JsonProperty("Id")
  private String id;

  @JsonProperty("Type")
  private String type;

  @JsonProperty("Names")
  private HashMap<String, String> names;

  @JsonProperty("ShortNames")
  private HashMap<String, String> shortName;
  
}
