package com.pn.booking.config.oauth2;

import java.util.Map;

/**
 * Author: chautn on 12/24/2020
 */
public interface ContextUserService {

  ContextUser getContextUser();

  Map<String, Object> jwtAttributes();
}
