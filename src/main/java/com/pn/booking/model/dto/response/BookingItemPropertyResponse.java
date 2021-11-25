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
@JsonInclude(Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingItemPropertyResponse {
  private Long id;
  private Long bookingItemId;
  private String source;
  private String externalId;
  private String value;
  private String type;
  private Timestamp createdAt;
  private Timestamp updatedAt;
}
