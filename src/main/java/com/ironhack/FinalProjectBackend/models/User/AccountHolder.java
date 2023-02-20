package com.ironhack.FinalProjectBackend.models.User;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.ironhack.FinalProjectBackend.models.bankAccounts.Account;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountHolder extends User{


    private LocalDate dateOfBirth;

    @Embedded
    private Address primaryAddress;



    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="streetName",column=@Column(name="mailing_street_name")),
            @AttributeOverride(name="streetNumber",column=@Column(name="mailing_street_number")),
            @AttributeOverride(name="postalCode",column=@Column(name="mailing_postal_code"))
    })
    private Address mailingAddress;

    @OneToMany (mappedBy= "primaryOwner", fetch = FetchType.EAGER)
    private List<Account> primanyOwnershipList = new ArrayList<>();

    @OneToMany (mappedBy = "secondaryOwner", fetch = FetchType.EAGER)
    private List <Account> secondaryOwnershipList = new ArrayList<>();

    public AccountHolder() {
    }

    public AccountHolder(String name, String username, String password, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name, username, password);
        setDateOfBirth(dateOfBirth);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(mailingAddress);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public List<Account> getPrimanyOwnershipList() {
        return primanyOwnershipList;
    }

    public void setPrimanyOwnershipList(List<Account> primanyOwnershipList) {
        this.primanyOwnershipList = primanyOwnershipList;
    }

    public List<Account> getSecondaryOwnershipList() {
        return secondaryOwnershipList;
    }

    public void setSecondaryOwnershipList(List<Account> secondaryOwnershipList) {
        this.secondaryOwnershipList = secondaryOwnershipList;
    }

    @Override
    public String toString() {
        return "AccountHolder{" +
                "dateOfBirth=" + dateOfBirth +
                ", primaryAddress=" + primaryAddress +
                ", mailingAddress=" + mailingAddress +
                ", primanyOwnershipList=" + primanyOwnershipList +
                ", secondaryOwnershipList=" + secondaryOwnershipList +
                '}';
    }
}
