package com.pn.booking.config.oauth2;

import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Author: chautn on 12/24/2020
 */
@Service
public class ContextUserServiceImpl implements ContextUserService {

  @Override
  public ContextUser getContextUser() {
    var attributes = jwtAttributes();
    if (attributes == null) {
      return null;
    }
    var user = new ContextUser();
    user.setUsername((String) attributes.get("username"));
    user.setClientId((String) attributes.get("client_id"));
    return user;
  }

  @Override
  public Map<String, Object> jwtAttributes() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken) {
      var jwtAuthentication = (JwtAuthenticationToken) authentication;
      return jwtAuthentication.getTokenAttributes();
    }
    // possible that the request was created without aaccess token (for example mews webhook request)
    // throw new InvalidBearerTokenException("Invalid access token");
    return null;
  }
}
