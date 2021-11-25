package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsResourceCategoryAssignmentResponse {
  
  @JsonProperty("Id")
  private String id;

  @JsonProperty("ResourceId")
  private String resourceId;

  @JsonProperty("CategoryId")
  private String categoryId;
  
}
