package com.ironhack.FinalProjectBackend.models.bankAccounts;


import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.enums.Status;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Checking extends Account {

    @NotNull
    private final BigDecimal minimumBalance = BigDecimal.valueOf(250);

    @NotNull
    private final BigDecimal monthlyMaintenanceFee = BigDecimal.valueOf(12);

    private String secretKey;

    public Checking() {
    }

    public Checking(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, LocalDate creationDate, Status status, String secretKey) {
        super(balance, primaryOwner, secondaryOwner,  creationDate, status);
        this.secretKey = secretKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void setBalance(BigDecimal balance){
        if (balance.compareTo(BigDecimal.valueOf(250)) < 0){
            super.setBalance(balance.subtract(BigDecimal.valueOf(40)));
        }else {
            super.setBalance(balance);
        }
    }
}
