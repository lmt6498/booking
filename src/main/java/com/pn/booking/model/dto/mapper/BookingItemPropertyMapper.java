package com.pn.booking.model.dto.mapper;

import com.pn.booking.model.dto.request.BookingItemPropertyRequest;
import com.pn.booking.model.entity.BookingItemProperty;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingItemPropertyMapper {

  BookingItemPropertyMapper INSTANCE = Mappers.getMapper(BookingItemPropertyMapper.class);

  BookingItemProperty toEntity(BookingItemPropertyRequest property);

}
