package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class BookingCustomerResponse {
  private Long id;
  private Long bookingId;
  private Long customerId;
  private Boolean isBooker;
  private Boolean isOwner;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private CustomerResponse customer;
}
