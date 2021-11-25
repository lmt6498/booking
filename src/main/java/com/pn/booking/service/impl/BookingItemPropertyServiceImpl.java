package com.pn.booking.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.pn.booking.model.dto.mapper.BookingItemPropertyMapper;
import com.pn.booking.model.dto.request.BookingItemPropertyRequest;
import com.pn.booking.model.entity.BookingItemProperty;
import com.pn.booking.repository.BookingItemPropertyRepository;
import com.pn.booking.service.BookingItemPropertyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingItemPropertyServiceImpl implements BookingItemPropertyService {
  
  @Autowired
  private BookingItemPropertyRepository bookingItemPropertyRepository;

  @Override
  public List<BookingItemProperty> createOrUpdate(List<BookingItemPropertyRequest> request) {
    List<BookingItemProperty> properties = new ArrayList<BookingItemProperty>();
    for (BookingItemPropertyRequest property : request) {
      BookingItemProperty existingProperty = bookingItemPropertyRepository
        .findFirstBySourceAndExternalIdAndBookingItemId(property.getSource(), property.getExternalId(), property.getBookingItemId());
      if (existingProperty != null) {
        property.setId(existingProperty.getId());
      }
      properties.add(bookingItemPropertyRepository.save(BookingItemPropertyMapper.INSTANCE.toEntity(property)));
    }
    return properties;
  }

  @Override
  public void deleteProperties(List<Long> bookingItemPropertyIds) {
    bookingItemPropertyRepository.deleteAllByIdIn(bookingItemPropertyIds);
  }
}
