package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsCustomerResponse {

  @JsonProperty("Id")
  private String id;

  @JsonProperty("FirstName")
  private String firstName;

  @JsonProperty("LastName")
  private String lastName;

  @JsonProperty("Email")
  private String email;

  @JsonProperty("Phone")
  private String phone;

  @JsonProperty("Sex")
  private String sex;

  @JsonProperty("NationalityCode")
  private String nationalityCode;

  @JsonProperty("BirthDate")
  private String birthdate;

  @JsonProperty("CompanyId")
  private String companyId;
  
}
