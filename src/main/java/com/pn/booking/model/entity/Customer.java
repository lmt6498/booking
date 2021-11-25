package com.pn.booking.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(CUEntityListener.class)
@Table(name = "customers")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE customers SET deleted_at = now() WHERE id = ?")
public class Customer implements CUEntity, Serializable {

  public enum Status {
    CHECKED_IN,
    CHECKED_OUT
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, updatable = false)
  private Long id;

  @Column(name = "source")
  private String source;

  @Column(name = "external_id")
  private String externalId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "mobile_number")
  private String mobileNumber;

  @Column(name = "gender")
  private String gender;

  @Column(name = "birthdate")
  private String birthdate;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  // join here
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<BookingCustomer> bookingCustomer;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
  private Set<CustomerProperty> properties;

}
