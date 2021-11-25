package com.pn.booking.model.dto.mapper;

import com.pn.booking.model.dto.request.CustomerPropertyRequest;
import com.pn.booking.model.entity.CustomerProperty;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerPropertyMapper {

  CustomerPropertyMapper INSTANCE = Mappers.getMapper(CustomerPropertyMapper.class);

  CustomerProperty toEntity(CustomerPropertyRequest property);

}
