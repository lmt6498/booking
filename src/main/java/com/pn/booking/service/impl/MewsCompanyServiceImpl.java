package com.pn.booking.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.pn.booking.model.dto.request.CustomerPropertyRequest;
import com.pn.booking.model.dto.request.mews.MewsCompaniesSearchRequest;
import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.dto.response.MewsCompanyListResponse;
import com.pn.booking.model.dto.response.MewsCompanyResponse;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.MewsCustomerResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.CustomerProperty;
import com.pn.booking.model.entity.Booking.Source;
import com.pn.booking.model.utils.RestTemplateUtils;
import com.pn.booking.model.utils.RestTemplateUtils.RestRequest;
import com.pn.booking.service.CustomerPropertyService;
import com.pn.booking.service.MewsCompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MewsCompanyServiceImpl implements MewsCompanyService {

  @Autowired
  private CustomerPropertyService customerPropertyService;

  @Value("${mews.get-all-companies-url}")
  private String getAllCompaniesUrl;

  @Value("${mews.client-token}")
  private String clientToken;

  public MewsCompanyListResponse fetchCompanies(MewsCredentialResponse credentials, List<String> companyIds) {
    MewsCompaniesSearchRequest searchRequest = MewsCompaniesSearchRequest.builder()
        .companyIds(companyIds)
        .accessToken(credentials.getAccessToken())
        .clientToken(clientToken)
        .build();
    
    RestRequest<MewsCompaniesSearchRequest> restRequest = new RestRequest<>(getAllCompaniesUrl, searchRequest);
    MewsCompanyListResponse response = RestTemplateUtils.post(restRequest, MewsCompanyListResponse.class);
    log.error("fetch detailed response from mews: ");
    log.error(new Gson().toJson(response));
    
    return response;
  }

  @Override
  public List<CustomerProperty> saveCompanies(Booking booking, List<CustomerResponse> savedCustomers, List<MewsCustomerResponse> customers, MewsCompanyListResponse response) {
    List<MewsCompanyResponse> companies = response.getCompanies();
    List<CustomerPropertyRequest> request = new ArrayList<>();
    for (MewsCompanyResponse company : companies) {
      CustomerPropertyRequest customerProperty = new CustomerPropertyRequest();

      MewsCustomerResponse customer = customers.stream()
        .filter(c -> company.getId().equals(c.getCompanyId())).findFirst().orElse(null);

      if (customer != null) {
        CustomerResponse savedCustomer = savedCustomers.stream()
          .filter(sc -> sc.getExternalId().equals(customer.getId()))
          .findFirst()
          .orElse(null);

        if (savedCustomer != null) {
          customerProperty.setCustomerId(savedCustomer.getId());
        }
      }
      customerProperty.setBookingId(booking.getId());
      customerProperty.setExternalId(company.getId());
      customerProperty.setType(CustomerProperty.Type.COMPANY.name());
      customerProperty.setSource(Source.MEWS.name());
      customerProperty.setValue(company.getName());
      request.add(customerProperty);
    }
    return customerPropertyService.createOrUpdate(request);
  }
}
