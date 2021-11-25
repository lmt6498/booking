package com.pn.booking.model.jpa.spec;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface FilterSpecBuilder<T> {

  Pageable getPageable();

  Specification<T> getSpec();

  Specification<T> getOrSpec();

}
