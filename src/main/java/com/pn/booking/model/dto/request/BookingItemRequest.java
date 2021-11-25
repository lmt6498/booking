package com.pn.booking.model.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.pn.booking.model.entity.BookingItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingItemRequest {
  private Long id;
  private Long bookingId;
  private String source;
  private String externalId;
  private String name;
  private BigDecimal itemAmount;
  private BigDecimal totalAmount;
  private String currency;
  private BookingItem.Type type;
  private Integer quantity;
  private List<BookingItemPropertyRequest> properties;
}
