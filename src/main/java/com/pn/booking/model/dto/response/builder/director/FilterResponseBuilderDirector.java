package com.pn.booking.model.dto.response.builder.director;

import com.pn.booking.model.dto.response.FilterResponse;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class FilterResponseBuilderDirector<T> {

  private final Page<?> result;

  public FilterResponseBuilderDirector(Page<?> result) {
    this.result = result;
  }

  public FilterResponse<T> buildPagination() {
    return FilterResponse.<T>builder()
        .page(result.getPageable().getPageNumber())
        .pageElements(result.getNumberOfElements())
        .size(result.getPageable().getPageSize())
        .totalPages(result.getTotalPages())
        .totalElements(result.getTotalElements())
        .orders(
            result.getPageable().getSort().stream()
                .map(order -> order.getProperty() + " " + order.getDirection().name())
                .collect(Collectors.toList())
        ).build();
  }
}
