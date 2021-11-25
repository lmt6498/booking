package com.pn.booking.model.dto.response.cloudbeds.webhook;

import lombok.Data;

@Data
public class DateChangeWebhook extends AbstractWebhookCloudbeds{
    private String startDate;
    private String endDate;
}
