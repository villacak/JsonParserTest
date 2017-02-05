package com.test.json.parser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by klausvillaca on 2/4/17.
 */
public class Patient {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("class")
    private String hospitalClass;

    @JsonProperty("profession")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profession;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getHospitalClass() {
        return hospitalClass;
    }

    public void setHospitalClass(String hospitalClass) {
        this.hospitalClass = hospitalClass;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Patient{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", familyName='").append(familyName).append('\'');
        sb.append(", hospitalClass='").append(hospitalClass).append('\'');
        sb.append(", profession='").append(profession).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
