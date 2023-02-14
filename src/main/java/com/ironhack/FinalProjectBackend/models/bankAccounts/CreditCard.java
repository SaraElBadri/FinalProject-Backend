package com.ironhack.FinalProjectBackend.models.bankAccounts;


import com.ironhack.FinalProjectBackend.models.Account;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CreditCard extends Account {

    private BigDecimal creditLimit = BigDecimal.valueOf(100);
    private double interestRate = 0.2;

    public CreditCard() {
    }

    public CreditCard(BigDecimal balance, String primaryOwner, LocalDate creationDate, BigDecimal creditLimit, double interestRate) {
        super(balance, primaryOwner, creationDate);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }


    //creditLimit higher than 100 but not higher than 100000
    public void setCreditLimit(BigDecimal creditLimit) {
        if(creditLimit.compareTo(BigDecimal.valueOf(100000)) <= 0 && creditLimit.compareTo(BigDecimal.valueOf(100)) >=0){
            this.creditLimit = creditLimit;
        } else {
            System.err.println("Credit Limit must be between 100 and 100000. Will be set by default to 100000. ");
            this.creditLimit = BigDecimal.valueOf(100000);
        }
    }

    public double getInterestRate() {
        return interestRate;
    }


    //An interestRate less than 0.2 but not lower than 0.1
    public void setInterestRate(double interestRate) {
        if(interestRate >= 0.1 && interestRate <= 0.2){
            this.interestRate = interestRate;
        } else {
            System.err.println("Interest rate must be between 0.1 and 0.2. Will be set by default to 0.1 ");
            this.interestRate = 0.1;
        }
    }
}
