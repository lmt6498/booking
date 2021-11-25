package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsServiceResponse {

  @Data
  public class MewsServiceData {

    @JsonProperty("Discriminator")
    private String discriminator;

  }

  public enum MewsServiceDataDiscriminator {
    @JsonProperty("Bookable")
    BOOKABLE,
    @JsonProperty("Additional")
    ADDITIONAL;
  }

  @JsonProperty("Id")
  private String id;

  @JsonProperty("IsActive")
  private Boolean isActive;

  @JsonProperty("Data")
  private MewsServiceData data;

  @JsonProperty("Name")
  private String name;
  
}
