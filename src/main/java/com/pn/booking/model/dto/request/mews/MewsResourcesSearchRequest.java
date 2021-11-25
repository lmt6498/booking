package com.pn.booking.model.dto.request.mews;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MewsResourcesSearchRequest extends MewsRequest {

  @JsonProperty("Extent")
  private Map<String, Boolean> extent;

}
