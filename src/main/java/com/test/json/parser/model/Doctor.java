package com.test.json.parser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by klausvillaca on 2/4/17.
 */
public class Doctor extends Patient {


    @JsonProperty("pager_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pagerNumber;

    @JsonProperty("staff_uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String staffUUID;

    public String getPagerNumber() {
        return pagerNumber;
    }

    public void setPagerNumber(String pagerNumber) {
        this.pagerNumber = pagerNumber;
    }

    public String getStaffUUID() {
        return staffUUID;
    }

    public void setStaffUUID(String staffUUID) {
        this.staffUUID = staffUUID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Doctor{");
        sb.append("pagerNumber='").append(pagerNumber).append('\'');
        sb.append(", staffUUID='").append(staffUUID).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
