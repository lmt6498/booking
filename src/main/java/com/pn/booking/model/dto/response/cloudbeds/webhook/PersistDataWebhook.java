package com.pn.booking.model.dto.response.cloudbeds.webhook;

import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.repository.BookingRepository;

public interface PersistDataWebhook {
    void doPersist(BookingRepository repository, MewsCredentialResponse credentialResponse, AbstractWebhookCloudbeds data);
}
