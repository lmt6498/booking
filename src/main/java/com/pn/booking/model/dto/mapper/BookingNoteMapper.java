package com.pn.booking.model.dto.mapper;

import java.util.List;

import com.pn.booking.model.dto.request.BookingNoteRequest;
import com.pn.booking.model.dto.response.BookingNoteResponse;
import com.pn.booking.model.entity.BookingNote;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingNoteMapper {
  BookingNoteMapper INSTANCE = Mappers.getMapper(BookingNoteMapper.class);

  BookingNote toEntity(BookingNoteRequest request);

  List<BookingNote> toEntities(List<BookingNoteRequest> request);

  List<BookingNoteResponse> toCreatedResponses(List<BookingNote> entities);

  BookingNoteResponse toResponse(BookingNote bookingNote);
}
