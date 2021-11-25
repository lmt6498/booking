package com.pn.booking.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsCredentialResponse {

  private Long id;

  private String username;

  private String enterpriseId;

  private String enterpriseName;

  private String serviceId;

  private String serviceName;

  private String accessToken;

  private Boolean isEnable;

  private String integrationId;

  private String integrationName;

  private String status;

  private String clientToken;

  private Long businessId;
}
