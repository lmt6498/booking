package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
public class BookingItemResponse {

  private Long id;
  private Long bookingId;
  private String source;
  private String externalId;
  private String name;
  private String type;
  private BigDecimal itemAmount;
  private BigDecimal totalAmount;
  private String currency;
  private Integer quantity;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private List<BookingItemPropertyResponse> properties;
}
