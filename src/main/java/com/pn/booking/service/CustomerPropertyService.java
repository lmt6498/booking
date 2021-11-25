package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.request.CustomerPropertyRequest;
import com.pn.booking.model.entity.CustomerProperty;

public interface CustomerPropertyService {
  public List<CustomerProperty> createOrUpdate(List<CustomerPropertyRequest> request);
}
