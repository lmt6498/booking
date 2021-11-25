package com.pn.booking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    final String[] authorities = new String[]{
        "SCOPE_booking/user",
        "SCOPE_connector/user",
        "SCOPE_connector/internal",
        "SCOPE_connector/system"
    };
    http.csrf().disable();
    http
        .authorizeRequests(authorize -> authorize
                .antMatchers("/actuator/health").hasIpAddress("localhost")
                .antMatchers("/bookings", "/bookings/**").hasAnyAuthority(authorities)
                .antMatchers("/mews/webhook").permitAll()
                .antMatchers("/cloudbeds/webhook", "/cloudbeds/webhook/**").permitAll()
                .anyRequest().denyAll()
                //.anyRequest().permitAll()
        )
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
      }
    };
  }

  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(
      @Autowired ClientRegistrationRepository clientRegistrationRepository,
      @Autowired OAuth2AuthorizedClientService authorizedClientService) {

    OAuth2AuthorizedClientProvider authorizedClientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .clientCredentials()
            .build();

    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
        new AuthorizedClientServiceOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientService);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

    return authorizedClientManager;
  }

  @Bean(name = "cognitoAuthorizedClient")
  public OAuth2AuthorizeRequest cognitoAuthorizedClient() {
    return OAuth2AuthorizeRequest
        .withClientRegistrationId("cognito")
        .principal("ms-booking-client")
        .build();
  }
}
