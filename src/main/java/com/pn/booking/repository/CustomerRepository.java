package com.pn.booking.repository;

import com.pn.booking.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
  Optional<Customer> findFirstBySourceAndExternalId(String source, String externalId);
}
