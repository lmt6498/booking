package com.pn.booking.model.dto.response;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsProductResponse {

  @Data
  public class MewsProductPrice {
    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("Value")
    private BigDecimal value;
  }

  @NotNull
  @JsonProperty("Id")
  private String id;

  @JsonProperty("ServiceId")
  private String serviceId;

  @JsonProperty("CategoryId")
  private String categoryId;

  @JsonProperty("IsActive")
  private Boolean isActive;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("ExternalName")
  private String externalName;

  @JsonProperty("ShortName")
  private String shortName;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("Price")
  private MewsProductPrice price;

  @JsonProperty("Charging")
  private String charging;

  @JsonProperty("Posting")
  private String posting;
}
