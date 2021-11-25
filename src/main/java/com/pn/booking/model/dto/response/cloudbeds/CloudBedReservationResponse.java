package com.pn.booking.model.dto.response.cloudbeds;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CloudBedReservationResponse {
    private int propertyID;
    private String guestName;
    private String guestID;
    private String guestEmail;
    private boolean isAnonymized;
    private String reservationID;
    private String dateCreated;
    private String dateModified;
    private String source;
    private String thirdPartyIdentifier;
    private String status;
    private int total;
    private int balance;
    private BalanceDetailed balanceDetailed;
    private List<Assigned> assigned;
    private List<Unassigned> unassigned;
    private String startDate;
    private String endDate;
    Map<Long, Guest> guestList;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class BalanceDetailed{
    private String suggestedDeposit;
    private int subTotal;
    private int additionalItems;
    private int taxesFees;
    private int grandTotal;
    private int paid;
}
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class DailyRate{
    private String date;
    private int rate;
}


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Room{
    private String roomTypeID;
    private String roomTypeName;
    private String roomID;
    private String roomName;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class UnassignedRoom{
    private String roomTypeID;
    private String roomTypeName;
}
