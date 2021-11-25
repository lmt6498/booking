package com.pn.booking.model.jpa.spec.builder;

import com.pn.booking.model.dto.request.filter.FilterRequest;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

public abstract class AbstractSpecBuilder<T> implements FilterSpecBuilder<T> {

  protected PageRequest pageable;
  protected final List<Specification<T>> specs = new ArrayList<>();

  public AbstractSpecBuilder() {
  }

  public AbstractSpecBuilder(FilterRequest filter, List<Specification<T>> specs) {
    this.buildPageable(filter);
    if (!CollectionUtils.isEmpty(specs)) {
      this.specs.addAll(specs);
    }
  }

  public Pageable getPageable() {
    return this.pageable;
  }

  public Specification<T> getSpec() {
    return getSpecWithRelation(false);
  }

  public Specification<T> getOrSpec() {
    return getSpecWithRelation(true);
  }

  private void buildPageable(FilterRequest request) {
    int page = 0;
    int size = 20;
    List<Order> orders = new ArrayList<>();
    if (request != null) {
      if (request.getPage() != null && request.getPage() >= 0) {
        page = request.getPage();
      }
      if (request.getSize() != null && request.getSize() > 0) {
        size = request.getSize();
      }
      if (request.getOrders() != null && request.getOrders().length > 0) {
        orders = parseOrders(request.getOrders());
      }
    }

    this.pageable = PageRequest.of(page, size, Sort.by(orders.toArray(new Order[0])));
  }

  protected Specification<T> getSpecWithRelation(boolean relationOr) {
    if (CollectionUtils.isEmpty(specs)) {
      return Specification.where(null);
    }
    Specification<T> spec = specs.get(0);
    for (int i = 1; i < specs.size(); i++) {
      if (specs.get(i) != null) {
        spec = relationOr ? spec.or(specs.get(i)) : spec.and(specs.get(i));
      }
    }
    return spec;
  }

  private List<Order> parseOrders(String[] strOrders) {
    List<Order> orders = new ArrayList<>();
    for (String strOrder : strOrders) {
      if (StringUtils.isNotBlank(strOrder)) {
        String[] strs = strOrder.trim().split(" ");
        String field = strs[0];
        Direction direction = strs.length < 2 ? Direction.ASC : Direction.fromString(strs[1]);
        orders.add(direction.isDescending() ? Order.desc(field) : Order.asc(field));
      }
    }
    return orders;
  }

}
