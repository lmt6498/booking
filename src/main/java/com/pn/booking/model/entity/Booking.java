package com.pn.booking.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pn.booking.model.entity.listener.CUEntity;
import com.pn.booking.model.entity.listener.CUEntityListener;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(CUEntityListener.class)
@Table(name = "bookings")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE bookings SET deleted_at = now() WHERE id = ?")
public class Booking implements CUEntity, Serializable {

  public enum Type {
    ONLINE,
    WALK_IN
  }

  public enum Source {
    MEWS,
    CLOUDBEDS
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, updatable = false)
  private Long id;

  @Column(name = "client_id")
  private String clientId;

  @Column(name = "source")
  private String source;

  @Column(name = "external_id")
  private String externalId;

  @Column(name = "business_id")
  private Long businessId;

  @Column(name = "business_type")
  private String businessType;

  @Column(name = "booking_number")
  private String bookingNumber;

  @Column(name = "start_time")
  private Timestamp startTime;

  @Column(name = "end_time")
  private Timestamp endTime;

  @Column(name = "raw_status")
  private String rawStatus;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private Type type;

  @Column(name = "status")
  private String status;

  @Column(name = "adult_count")
  private Integer adultCount;

  @Column(name = "child_count")
  private Integer childCount;

  @Column(name = "last_message")
  private String lastMessage;

  @Column(name = "company")
  private String company;

  @Column(name = "integration_credential_id")
  private Long integrationCredentialId;

  @Column(name = "enterprise_id")
  private String enterpriseId;

  @Column(name = "reason_for_stay")
  private String reasonForStay;

  @Column(name = "booked_at")
  private Timestamp bookedAt;

  @Column(name = "cancelled_at")
  private Timestamp cancelledAt;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Booking)) {
      return false;
    }
    Booking booking = (Booking) o;
    return getId().equals(booking.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking", cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<BookingCustomer> bookingCustomers;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking", cascade = CascadeType.ALL)
  private Set<BookingItem> bookingItems;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "booking")
  private Set<BookingNote> bookingNotes;

}
