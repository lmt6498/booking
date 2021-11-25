package com.pn.booking.client.feign.config;

import feign.FeignException.FeignClientException;
import lombok.extern.slf4j.Slf4j;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.time.Instant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@Slf4j
public class CognitoClientInterceptor implements RequestInterceptor {

  private final OAuth2AuthorizedClientManager authorizedClientManager;
  private final OAuth2AuthorizedClientService authorizedClientService;
  private final OAuth2AuthorizeRequest cognitoAuthorizedClient;

  public CognitoClientInterceptor(OAuth2AuthorizedClientManager authorizedClientManager,
      OAuth2AuthorizedClientService authorizedClientService, OAuth2AuthorizeRequest cognitoAuthorizedClient) {
    this.authorizedClientManager = authorizedClientManager;
    this.authorizedClientService = authorizedClientService;
    this.cognitoAuthorizedClient = cognitoAuthorizedClient;
  }

  @Override
  public void apply(RequestTemplate requestTemplate) {
    OAuth2AccessToken accessToken = getAccessToken();
    if (accessToken == null) {
      throw new FeignClientException(
          HttpStatus.UNAUTHORIZED.value(),
          "Could not authorize ms-booking-client",
          requestTemplate.request(),
          null
      );
    }
    //feign client exclude add AUTHORIZATION header
    if(requestTemplate.feignTarget().name().equals("CloudbedClient")){
      return;
    }
    log.error("accessToken: " + accessToken.getTokenType().getValue() + accessToken.getTokenValue());

    requestTemplate.header(
        HttpHeaders.AUTHORIZATION,
        accessToken.getTokenType().getValue() + " " + accessToken.getTokenValue()
    );
  }

  private OAuth2AccessToken getAccessToken() {
    OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
        cognitoAuthorizedClient.getClientRegistrationId(),
        cognitoAuthorizedClient.getPrincipal().getName()
    );
    OAuth2AccessToken accessToken = client != null ? client.getAccessToken() : null;
    if (accessToken == null || accessToken.getExpiresAt() == null || accessToken.getExpiresAt().isBefore(Instant.now())) {
      client = authorizedClientManager.authorize(cognitoAuthorizedClient);
      accessToken = client != null ? client.getAccessToken() : null;
    }
    return accessToken;
  }
}
