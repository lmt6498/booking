package com.pn.booking.model.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsAmountValueResponse {

  @JsonProperty("Currency")
  private String currency;

  @JsonProperty("NetValue")
  private BigDecimal netValue;

  @JsonProperty("GrossValue")
  private BigDecimal grossValue;

}
