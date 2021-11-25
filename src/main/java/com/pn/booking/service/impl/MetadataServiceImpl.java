package com.pn.booking.service.impl;

import com.pn.booking.model.dto.enumeration.MetadataType;
import com.pn.booking.model.entity.Metadata;
import com.pn.booking.model.entity.Booking.Source;
import com.pn.booking.repository.MetadataRepository;
import com.pn.booking.service.MetadataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetadataServiceImpl implements MetadataService {

  @Autowired
  private MetadataRepository metadataRepository;
  
  @Override
  public Metadata get(Long businessId, MetadataType type, Source source) {
    return metadataRepository.findFirstBySourceAndBusinessIdAndType(source, businessId, type);
  }

  @Override
  public Metadata create(Long businessId, MetadataType type, Source source, String data) {
    Metadata metadata = new Metadata();
    metadata.setBusinessId(businessId);
    metadata.setType(type);
    metadata.setSource(source);
    metadata.setData(data);
    metadataRepository.save(metadata);
    return metadata;
  }
  
}
