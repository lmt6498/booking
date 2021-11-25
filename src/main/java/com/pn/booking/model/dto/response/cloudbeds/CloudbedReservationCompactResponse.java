package com.pn.booking.model.dto.response.cloudbeds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudbedReservationCompactResponse {
   private String reservationID;
   private String status;
}
