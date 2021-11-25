package com.pn.booking.model.dto.request.mews.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pn.booking.model.dto.enumeration.MewsEventDiscriminator;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsEventRequest {

  @Getter
  @Setter
  public static class MewsValueRequest {

    @JsonProperty("Id")
    private String id;
  }

  @NotNull
  @JsonProperty("Discriminator")
  private MewsEventDiscriminator discriminator;

  @NotNull
  @JsonProperty("Value")
  private MewsValueRequest value;
}
