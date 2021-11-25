package com.pn.booking.repository;

import com.pn.booking.model.entity.BookingCustomer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingCustomerRepository extends JpaRepository<BookingCustomer, Long>, JpaSpecificationExecutor<BookingCustomer> {

  List<BookingCustomer> findByBookingId(Long bookingId);

}
