package com.pn.booking.model.dto.request.mews.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MewsIntegrationDataRequest {

  @Getter
  @Setter
  public static class MewsIntegrationDataItem {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;
  }

  @Getter
  @Setter
  public static class MewsIntegrationRequestor {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Email")
    private String email;
  }

  @JsonProperty("Enterprise")
  private MewsIntegrationDataItem enterprise;

  @JsonProperty("Service")
  private MewsIntegrationDataItem service;

  @JsonProperty("Requestor")
  private MewsIntegrationRequestor requestor;

  @JsonProperty("AccessToken")
  private String accessToken;

  @JsonProperty("CreatedUtc")
  private String createdUtc;

  @JsonProperty("IsEnabled")
  private Boolean isEnabled;

  @JsonProperty("Integration")
  private MewsIntegrationDataItem integration;
}
