package com.pn.booking.repository;

import com.pn.booking.model.dto.enumeration.MetadataType;
import com.pn.booking.model.entity.Metadata;
import com.pn.booking.model.entity.Booking.Source;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface MetadataRepository extends CrudRepository<Metadata, Long>, JpaSpecificationExecutor<Metadata> {
 
  public Metadata findFirstBySourceAndBusinessIdAndType(Source source, Long businessId, MetadataType type);
  
}
