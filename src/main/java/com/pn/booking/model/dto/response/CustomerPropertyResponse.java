package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CustomerPropertyResponse {
  private Long id;
  private Long customerId;
  private Long bookingId;
  private String source;
  private String externalId;
  private String type;
  private String value;
  private Timestamp createdAt;
  private Timestamp updatedAt;
}
