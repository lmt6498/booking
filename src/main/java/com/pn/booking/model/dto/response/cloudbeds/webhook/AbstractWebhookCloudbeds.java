package com.pn.booking.model.dto.response.cloudbeds.webhook;

import lombok.Data;

@Data
public abstract class AbstractWebhookCloudbeds{
    private String version;
    private String timestamp;
    private String event;
    private String propertyId;
    private String reservationId;
}
