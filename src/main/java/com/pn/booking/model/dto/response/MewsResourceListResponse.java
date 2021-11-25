package com.pn.booking.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MewsResourceListResponse {
  
  @JsonProperty("Resources")
  private List<MewsResourceResponse> resources;

  @JsonProperty("ResourceCategories")
  private List<MewsResourceCategoryResponse> resourceCategories;

  @JsonProperty("ResourceCategoryAssignments")
  private List<MewsResourceCategoryAssignmentResponse> resourceCategoryAssignments;

}
