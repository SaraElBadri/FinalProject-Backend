package com.ironhack.FinalProjectBackend.models.User;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String streetName;
    private String streetNumber;

    private String postalCode;

    public Address() {
    }

    public Address(String streetName, String streetNumber, String postalCode) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
