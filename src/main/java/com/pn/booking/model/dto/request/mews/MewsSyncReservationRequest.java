package com.pn.booking.model.dto.request.mews;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MewsSyncReservationRequest {

  private String username;

  private String enterpriseId;

  private List<String> reservationIds;

  private List<String> numbers;

  private String lastMessageAsJson;
}
