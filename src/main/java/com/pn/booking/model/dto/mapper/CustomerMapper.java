package com.pn.booking.model.dto.mapper;

import java.util.List;

import com.pn.booking.model.dto.request.CustomerRequest;
import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.entity.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

  CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

  Customer toEntity(CustomerRequest customer);

  List<Customer> toEntities(List<CustomerRequest> customers);

  CustomerResponse toResponse(Customer customer);

  List<CustomerResponse> toResponses(List<Customer> customers);

}
