package com.pn.booking.config.helper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelperConfig {

  @Bean
  public AutowireHelper autowireHelper() {
    return AutowireHelper.getInstance();
  }

}
