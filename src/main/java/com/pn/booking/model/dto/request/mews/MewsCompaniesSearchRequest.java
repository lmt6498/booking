package com.pn.booking.model.dto.request.mews;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MewsCompaniesSearchRequest extends MewsRequest {
  
  @JsonProperty("Ids")
  private List<String> companyIds;

}
