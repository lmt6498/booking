package com.pn.booking.service.impl;

import com.pn.booking.model.dto.mapper.CustomerMapper;
import com.pn.booking.model.dto.request.CustomerRequest;
import com.pn.booking.model.dto.request.filter.GuestFilterRequest;
import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.dto.response.FilterResponse;
import com.pn.booking.model.dto.response.builder.director.FilterResponseBuilderDirector;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.BookingHasCustomer;
import com.pn.booking.model.entity.Customer;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;
import com.pn.booking.model.jpa.spec.director.GuestFilterSpecDirector;
import com.pn.booking.repository.BookingHasCustomerRepository;
import com.pn.booking.repository.CustomerRepository;
import com.pn.booking.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private BookingHasCustomerRepository bookingHasCustomerRepository;

  @Override
  public CustomerResponse get(Long customerId) {
    log.info("Getting booking guest by id {}", customerId);
    return CustomerMapper.INSTANCE.toResponse(getEntity(customerId));
  }

  @Transactional
  @Override
  public List<CustomerResponse> create(Booking booking, List<CustomerRequest> requestList) {
    log.error("Creating {} guests for booking {}", requestList.size(), booking.getId());
    if (CollectionUtils.isEmpty(requestList)) {
      return Collections.emptyList();
    }

    List<Customer> bookingCustomers = CustomerMapper.INSTANCE.toEntities(requestList);
    if (!CollectionUtils.isEmpty(bookingCustomers)) {
      for (CustomerRequest customerRequest : requestList) {
        Customer customer = customerRepository.save(CustomerMapper.INSTANCE.toEntity(customerRequest));
        BookingHasCustomer bhc = new BookingHasCustomer();
        bhc.setCustomersId(customer.getId());
        bhc.setBookingId(booking.getId());
        bhc.setIsOwner(customerRequest.getIsOwner());
        bhc.setIsBooker(customerRequest.getIsBooker());
        bookingHasCustomerRepository.save(bhc);
      }
    }

    return bookingCustomers.stream()
        .map(bookingCustomer -> CustomerMapper.INSTANCE.toResponse(bookingCustomer))
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public List<Customer> createOrUpdate(List<CustomerRequest> request) {
    List<Customer> customers = CustomerMapper.INSTANCE.toEntities(request);
    List<Customer> customersForSaving = new ArrayList<Customer>();
    if (!CollectionUtils.isEmpty(customers)) {
      for (Customer customer : customers) {
        Customer customerEntity = customerRepository.findFirstBySourceAndExternalId(customer.getSource(), customer.getExternalId()).get();
        if (customerEntity != null) {
           customer.setId(customerEntity.getId());
        }
        customersForSaving.add(customer);
      }
      customersForSaving = customerRepository.saveAll(customersForSaving);
    }
    return customersForSaving;
  }

  @Transactional
  @Override
  public CustomerResponse update(Long bookingGuestId, CustomerRequest request) {
    return null;
  }

  @Override
  public FilterResponse<CustomerResponse> filter(GuestFilterRequest filter) {
    log.info("Filtering guests: " + filter.toString());
    FilterSpecBuilder<Customer> filterSpec = new GuestFilterSpecDirector(filter).build();
    Page<Customer> result = customerRepository.findAll(filterSpec.getSpec(), filterSpec.getPageable());

    FilterResponse<CustomerResponse> filterResponse = new FilterResponseBuilderDirector<CustomerResponse>(result).buildPagination();
    filterResponse.setData(
        result.toList().stream()
            .map(guest -> CustomerMapper.INSTANCE.toResponse(guest))
            .collect(Collectors.toList())
    );
    return filterResponse;
  }

  private Customer getEntity(Long bookingGuestId) {
    return customerRepository
        .findById(bookingGuestId)
        .orElseThrow(() -> new IllegalArgumentException("Booking guest not found: " + bookingGuestId));
  }

  @Transactional
  @Override
  public CustomerResponse remove(Long customerId) {
    Customer guest = getEntity(customerId);
    this.customerRepository.delete(guest);
    return CustomerMapper.INSTANCE.toResponse(guest);
  }

  @Override
  public void removeInBooking(Long bookingId, List<Long> customerIds) {
    bookingHasCustomerRepository.deleteAllByCustomerIdInAndBookingId(customerIds, bookingId);
  }

}
