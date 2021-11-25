package com.pn.booking.client.feign.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@Configuration
public class CognitoClientConfig {

  @Autowired
  private OAuth2AuthorizedClientManager authorizedClientManager;

  @Autowired
  private OAuth2AuthorizedClientService authorizedClientService;

  @Autowired
  @Qualifier("cognitoAuthorizedClient")
  private OAuth2AuthorizeRequest cognitoAuthorizedClient;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return new CognitoClientInterceptor(authorizedClientManager, authorizedClientService, cognitoAuthorizedClient);
  }
}
