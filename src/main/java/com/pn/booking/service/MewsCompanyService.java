package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.dto.response.MewsCompanyListResponse;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.MewsCustomerResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.CustomerProperty;

public interface MewsCompanyService {
  public List<CustomerProperty> saveCompanies(Booking booking, List<CustomerResponse> savedCustomers, List<MewsCustomerResponse> customers, MewsCompanyListResponse response);
  public MewsCompanyListResponse fetchCompanies(MewsCredentialResponse credentials, List<String> companyIds);
}
