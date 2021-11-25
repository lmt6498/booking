package com.pn.booking.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsCompanyListResponse {
  
  @JsonProperty("Companies")
  private List<MewsCompanyResponse> companies;

}
