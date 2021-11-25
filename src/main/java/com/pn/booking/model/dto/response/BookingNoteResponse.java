package com.pn.booking.model.dto.response;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class BookingNoteResponse {
  private Long id;
  private Long bookingId;
  private String externalId;
  private String source;
  private String notes;
  private Timestamp createdAt;
  private Timestamp updatedAt;
}
