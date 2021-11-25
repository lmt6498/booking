package com.pn.booking.model.dto.mapper;

import java.util.List;

import com.pn.booking.model.dto.request.BookingRequest;
import com.pn.booking.model.dto.response.BookingResponse;
import com.pn.booking.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author chautn
 */
@Mapper
public interface BookingMapper {

  BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

  Booking toEntity(BookingRequest request);

  BookingResponse toResponse(Booking booking);

  List<BookingResponse> toResponses(List<Booking> bookings);
}
