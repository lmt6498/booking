package com.pn.booking.model.dto.request;

import lombok.Data;

@Data
public class CustomerPropertyRequest {
  
  private Long id;
  private Long customerId;
  private Long bookingId;
  private String source;
  private String externalId;
  private String type;
  private String value;

}
