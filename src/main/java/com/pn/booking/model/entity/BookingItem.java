package com.pn.booking.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pn.booking.model.entity.listener.CUEntity;
import com.pn.booking.model.entity.listener.CUEntityListener;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor 
@EntityListeners(CUEntityListener.class)
@Table(name = "booking_items")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE booking_items SET deleted_at = now() WHERE id = ?")
public class BookingItem implements CUEntity, Serializable {

  public enum Type {
    PLACE,
    PRODUCT,
    TICKET
  }

  public enum Status{
    ASSIGNED,
    UNASSIGNED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, updatable = false)
  private Long id;

  @Column(name = "bookings_id")
  private Long bookingId;

  @Column(name = "source")
  private String source;

  @Column(name = "external_id")
  private String externalId;

  @Column(name = "item_amount")
  private BigDecimal itemAmount;

  @Column(name = "total_amount")
  private BigDecimal totalAmount;

  @Column(name = "currency")
  private String currency;

  @Column(name = "name")
  private String name;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private Type type;

  @Column(name = "item_status")
  @Enumerated(EnumType.STRING)
  private Status itemStatus;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bookings_id", referencedColumnName = "id", updatable = false, insertable = false)
  private Booking booking;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookingItem")
  private Set<BookingItemProperty> properties;
  
}
