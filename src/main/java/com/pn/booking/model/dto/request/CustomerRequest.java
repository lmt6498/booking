package com.pn.booking.model.dto.request;

import java.util.Date;

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
public class CustomerRequest {

  private Long id;
  private String source;
  private String externalId;
  private String firstName;
  private String lastName;
  private String email;
  private String mobileNumber;
  private Boolean isOwner;
  private Boolean isBooker;
  private String gender;
  private String birthdate;

}
