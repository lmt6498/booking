package com.pn.booking.model.dto.mapper;

import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.response.BookingItemResponse;
import com.pn.booking.model.entity.BookingItem;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingItemMapper {

  BookingItemMapper INSTANCE = Mappers.getMapper(BookingItemMapper.class);

  BookingItem toEntity(BookingItemRequest request);

  List<BookingItem> toEntities(List<BookingItemRequest> list);

  @Named("toCreatedResponses")
  @IterableMapping(qualifiedByName = "toCreatedResponse")
  List<BookingItemResponse> toResponses(List<BookingItem> entities);

  @Named("toCreatedResponse")
  @Mappings({
      @Mapping(target = "bookingId", source = "booking.id")
  })
  BookingItemResponse toResponse(BookingItem entity);
}
