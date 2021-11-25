package com.pn.booking.service;

import com.pn.booking.model.dto.enumeration.MetadataType;
import com.pn.booking.model.entity.Metadata;
import com.pn.booking.model.entity.Booking.Source;

public interface MetadataService {

  Metadata get(Long businessId, MetadataType type, Source source);
  Metadata create(Long businessId, MetadataType type, Source source, String data);

}
