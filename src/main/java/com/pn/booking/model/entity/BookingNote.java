package com.pn.booking.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pn.booking.model.entity.listener.CUEntity;
import com.pn.booking.model.entity.listener.CUEntityListener;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(CUEntityListener.class)
@Table(name = "booking_notes")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE booking_notes SET deleted_at = now() WHERE id = ?")
public class BookingNote implements CUEntity, Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, updatable = false)
  private Long id;

  @Column(name = "bookings_id")
  private Long bookingId;

  @Column(name = "external_id")
  private String externalId;

  @Column(name = "source")
  private String source;

  @Column(name = "notes")
  private String notes;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bookings_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Booking booking;

}
