package com.pn.booking.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.pn.booking.model.entity.Customer;

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
@Table(name = "bookings_has_customers")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE bookings_has_customers SET deleted_at = now() WHERE id = ?")
public class BookingHasCustomer implements CUEntity, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, updatable = false)
  private Long id;

  @Column(name = "bookings_id")
  private Long bookingId;

  @Column(name = "customers_id")
  private Long customersId;

  @Column(name = "is_booker")
  private Boolean isBooker;

  @Column(name = "is_owner")
  private Boolean isOwner;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", referencedColumnName = "id", updatable = false, insertable = false)
  private Customer customer;
  
}
