package com.pn.booking.model.dto.response.cloudbeds.webhook;

import lombok.Data;

@Data
public class StatusChangedWebhook extends AbstractWebhookCloudbeds{
    private String status;


}
