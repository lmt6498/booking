

package com.pn.booking.repository;

import com.pn.booking.model.entity.BookingHasCustomer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingHasCustomerRepository extends JpaRepository<BookingHasCustomer, Long>, JpaSpecificationExecutor<BookingHasCustomer> {

  List<BookingHasCustomer> findAllByBookingId(Long bookingId);

  void deleteAllByCustomerIdInAndBookingId(List<Long> customerIds, Long bookingId);

}
