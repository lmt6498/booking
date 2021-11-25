package com.pn.booking.model.dto.request;

import com.pn.booking.model.entity.BookingItemProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingItemPropertyRequest {
  private Long id;
  private Long bookingItemId;
  private String source;
  private String externalId;
  private String value;
  private BookingItemProperty.Type type;
  private Boolean remove;
}
