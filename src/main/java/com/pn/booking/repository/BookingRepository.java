package com.pn.booking.repository;

import com.pn.booking.model.entity.Booking;
import com.pn.booking.repository.custom.BookingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends CrudRepository<Booking, Long>, JpaSpecificationExecutor<Booking>, BookingRepositoryCustom {

  List<Booking> findBySourceAndExternalIdIn(String source, List<String> externalIds);

  Optional<Booking> findFirstBySourceAndExternalId(String source, String externalId);

  List<Booking> findAllByBusinessId(Long businessId);

  Booking findFirstById(Long bookingId);

}
