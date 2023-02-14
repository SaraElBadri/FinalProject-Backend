package com.ironhack.FinalProjectBackend.models.bankAccounts;


import com.ironhack.FinalProjectBackend.models.Account;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Savings  extends Account {



    //Savings accounts have a default interest rate of 0.0025

    @NotNull
    private double interestRate = 0.0025;


    //Savings accounts should have a default minimumBalance of 1000
    @NotNull
    private BigDecimal minimumBalance = BigDecimal.valueOf(1000);

    public Savings() {
    }

    public Savings(BigDecimal balance, String primaryOwner, LocalDate creationDate, double interestRate, BigDecimal minimumBalance) {
        super(balance, primaryOwner, creationDate);
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
    }

    public double getInterestRate() {
        return interestRate;
    }


    //Savings with an interest rate other than the default, with a maximum interest rate of 0.5.
    public void setInterestRate(double interestRate) {
       if (interestRate <= 0.5 ){
           this.interestRate = interestRate;
       } else {
           System.err.println("Interest rate should be higher than 0.0025 but lower than 0.5");
           this.interestRate = 0.5;
       }
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }


    // A minimum balance of less than 1000 but no lower than 100
    public void setMinimumBalance(BigDecimal minimumBalance) {
        if (minimumBalance.compareTo(new BigDecimal(100)) > 0){
            this.minimumBalance = minimumBalance;
        } else{
            System.err.println("Minimum balance can't be lower than 100. It's gonna be set by default to 100.");
            this.minimumBalance = new BigDecimal(100);
        }
    }
}
