package com.pn.booking.model.dto.response;

import java.util.List;

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
public class CustomerResponse {
  private Long id;
  private String externalId;
  private String firstName;
  private String lastName;
  private String email;
  private String mobileNumber;
  private String status;
  private Boolean isOwner;
  private Boolean isBooker;
  private Long bookingId;
  private String gender;
  private String birthdate;
  private List<CustomerPropertyResponse> properties;
}
