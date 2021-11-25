package com.pn.booking.service;

import java.util.List;

public interface WebhookService {
  
  public boolean fireWebhook(String event, Long businessId, String businessType);

}
