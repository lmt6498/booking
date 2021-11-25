package com.pn.booking.model.dto.request.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FilterRequest {

  private String searchKey;
  private Integer page;
  private Integer size;
  private String[] orders;
}
