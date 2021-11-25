package com.pn.booking.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.pn.booking.model.dto.mapper.CustomerPropertyMapper;
import com.pn.booking.model.dto.request.CustomerPropertyRequest;
import com.pn.booking.model.entity.CustomerProperty;
import com.pn.booking.repository.CustomerPropertyRepository;
import com.pn.booking.service.CustomerPropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerPropertyServiceImpl implements CustomerPropertyService {
  
  @Autowired
  private CustomerPropertyRepository customerPropertyRepository;

  @Override
  public List<CustomerProperty> createOrUpdate(List<CustomerPropertyRequest> request) {
    List<CustomerProperty> customerProperties = new ArrayList<CustomerProperty>();
    for (CustomerPropertyRequest property : request) {
      CustomerProperty existingCustomerProperty = customerPropertyRepository
        .findFirstBySourceAndExternalIdAndBookingId(property.getSource(), property.getExternalId(), property.getBookingId());
      if (existingCustomerProperty != null) {
        property.setId(existingCustomerProperty.getId());
      }
      customerProperties.add(customerPropertyRepository.save(CustomerPropertyMapper.INSTANCE.toEntity(property)));
    }
    return customerProperties;
  }

}
