package com.pn.booking.model.dto.request;

import lombok.Data;

@Data
public class WebhookCallbackRequest {
  String event;
  Long businessId;
  String businessType;
}
