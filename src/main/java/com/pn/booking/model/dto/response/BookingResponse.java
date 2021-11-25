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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BookingResponse {

  private Long id;
  private String source;
  private String externalId;
  private Long businessId;
  private String bookingNumber;
  private Timestamp startTime;
  private Timestamp endTime;
  private String type;
  private String status;
  private Integer adultCount;
  private Integer childCount;
  private Long integrationCredentialId;
  private String enterpriseId;
  private String reasonForStay;
  private String company;
  private Timestamp bookedAt;
  private Timestamp cancelledAt;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  private List<BookingCustomerResponse> bookingCustomers;
  private List<BookingItemResponse> bookingItems;
  private List<BookingNoteResponse> bookingNotes;

}
