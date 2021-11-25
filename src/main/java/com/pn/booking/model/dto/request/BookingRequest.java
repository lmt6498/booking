package com.pn.booking.model.dto.request;

import java.sql.Timestamp;
import java.util.List;

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
public class BookingRequest {
  private Long id;
  private String source;
  private String externalId;
  private Long businessId;
  private String businessType;
  private String bookingNumber;
  private Timestamp startTime;
  private Timestamp endTime;
  private String type;
  private String status;
  private Integer adultCount;
  private Integer childCount;
  private String lastMessage;
  private Long integrationCredentialId;
  private String enterpriseId;
  private String reasonForStay;
  private String company;
  private Timestamp bookedAt;
  private Timestamp cancelledAt;

  private List<CustomerRequest> customers;
  private List<BookingItemRequest> items;
  private List<BookingNoteRequest> notes;
}
