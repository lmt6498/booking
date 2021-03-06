package com.pn.booking.model.dto.request;

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
public class BookingNoteRequest {
  private Long id;
  private Long bookingId;
  private String externalId;
  private String source;
  private String notes;
}
