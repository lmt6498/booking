package com.pn.booking.model.dto.response.cloudbeds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Guest {
    private String guestID;
    private String guestFirstName;
    private String guestLastName;
    private String guestGender;
    private String guestEmail;
    private String guestPhone;
    private String guestCellPhone;
    private String guestCountry;
    private String guestAddress;
    private String guestAddress2;
    private String guestCity;
    private String guestZip;
    private String guestState;
    private String guestStatus;
    private String guestBirthdate;
    private String guestDocumentType;
    private String guestDocumentNumber;
    private String guestDocumentIssueDate;
    private String guestDocumentIssuingCountry;
    private String guestDocumentExpirationDate;
    private boolean assignedRoom;
    private boolean isMainGuest;
    private boolean isAnonymized;
    private String taxID;
    private String companyTaxID;
    private String companyName;
}
