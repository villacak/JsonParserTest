package com.test.json.parser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by klausvillaca on 2/4/17.
 */
public class Nurse extends Patient {

    @JsonProperty("ward_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int wardNumber;

    @JsonProperty("staff_uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String staffUUID;

    public int getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String getStaffUUID() {
        return staffUUID;
    }

    public void setStaffUUID(String staffUUID) {
        this.staffUUID = staffUUID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Nurse{");
        sb.append("wardNumber=").append(wardNumber);
        sb.append(", staffUUID='").append(staffUUID).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
