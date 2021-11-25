package com.pn.booking.model.dto.response.cloudbeds.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pn.booking.common.utils.CloudbedsUtils;
import com.pn.booking.model.constant.BookingStatus;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.cloudbeds.Assigned;
import com.pn.booking.model.dto.response.cloudbeds.CloudBedReservationResponse;
import com.pn.booking.model.dto.response.cloudbeds.Guest;
import com.pn.booking.model.dto.response.cloudbeds.Unassigned;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.BookingCustomer;
import com.pn.booking.model.entity.BookingItem;
import com.pn.booking.model.entity.Customer;
import com.pn.booking.repository.BookingRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

@Data
public class CreatedWebhooksCloudbeds extends AbstractWebhookCloudbeds {
    private String startDate;
    private String endDate;
}
