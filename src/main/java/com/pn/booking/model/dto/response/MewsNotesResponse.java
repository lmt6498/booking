package com.pn.booking.model.dto.response;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsNotesResponse {
  
  @JsonProperty("Id")
  private String id;

  @JsonProperty("OrderId")
  private String orderId;

  @JsonProperty("Text")
  private String text;

  @JsonProperty("Type")
  private String type;

  @JsonProperty("CreatedUtc")
  private Timestamp CreatedUtc;

}
