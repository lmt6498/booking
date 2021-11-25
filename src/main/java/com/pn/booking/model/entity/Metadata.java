package com.pn.booking.model.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pn.booking.model.dto.enumeration.MetadataType;
import com.pn.booking.model.entity.Booking.Source;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "metadata")
public class Metadata implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, updatable = false)
  private Long id;

  @Column(name = "business_id")
  private Long businessId;

  @Column(name = "external_id")
  private String externalId;

  @Column(name = "source")
  private Source source;

  @Column(name = "type")
  private MetadataType type;

  @Column(name = "data")
  private String data;

}
