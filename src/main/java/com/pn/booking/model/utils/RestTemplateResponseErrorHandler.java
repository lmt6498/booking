package com.pn.booking.model.utils;

import com.pn.booking.model.dto.response.MewsErrorResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    return clientHttpResponse.getStatusCode().series() == Series.CLIENT_ERROR
        || clientHttpResponse.getStatusCode().series() == Series.SERVER_ERROR;
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    if (clientHttpResponse.getStatusCode().series() == Series.SERVER_ERROR || clientHttpResponse.getStatusCode().series() == Series.CLIENT_ERROR) {
      MewsErrorResponse errorResponse = ObjectMapperUtils.convertInputStreamValue(clientHttpResponse.getBody(), MewsErrorResponse.class);
      throw new IllegalArgumentException(errorResponse.getMessage());
    }
  }
}
