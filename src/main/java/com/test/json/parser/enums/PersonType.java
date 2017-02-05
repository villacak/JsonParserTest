package com.test.json.parser.enums;

/**
 * Created by klausvillaca on 2/4/17.
 */
public enum PersonType {

    PATIENT("Patient"), NURSE("Nurse"), DOCTOR("Doctor");

    private String type;

    private PersonType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
