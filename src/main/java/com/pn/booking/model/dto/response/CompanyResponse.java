package com.pn.booking.model.dto.response;

import lombok.Data;

@Data
public class CompanyResponse {
  
  private Long id;
  private String externalId;
  private String name;
  private String source;
  
}
