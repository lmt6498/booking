package com.pn.booking.model.dto.response.cloudbeds.webhook;

import lombok.Data;

@Data
public class AccomStatusChangeWebhook extends AbstractWebhookCloudbeds{
    private String roomId;
    private String status;
}
