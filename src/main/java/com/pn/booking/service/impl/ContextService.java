package com.pn.booking.service.impl;

import com.pn.booking.config.oauth2.ContextUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class ContextService {

  @Autowired
  private ContextUserService contextUserService;

  protected String clientId() {
    return this.contextUserService.getContextUser().getClientId();
  }

  protected String username() {
    return this.contextUserService.getContextUser().getUsername();
  }
}
