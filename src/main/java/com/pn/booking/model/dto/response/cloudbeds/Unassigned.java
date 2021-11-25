package com.pn.booking.model.dto.response.cloudbeds;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Unassigned {
    private String roomTypeName;
    private String roomTypeNameShort;
    private String roomTypeID;
    private String subReservationID;
    private String startDate;
    private String endDate;
    private String adults;
    private String children;
    private List<DailyRate> dailyRates;
    private String roomTotal;
}
