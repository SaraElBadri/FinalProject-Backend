package com.ironhack.FinalProjectBackend.models.bankAccounts;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.enums.Status;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Entity
public class Savings  extends Account {



    //Savings accounts have a default interest rate of 0.0025

    @NotNull
    private BigDecimal interestRate = BigDecimal.valueOf(0.0025);


    //Savings accounts should have a default minimumBalance of 1000
    @NotNull
    private BigDecimal minimumBalance = BigDecimal.valueOf(1000);

    private String secretKey;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastInterest = getCreationDate();

    public Savings() {
    }

    public Savings(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,  LocalDate creationDate, Status status, BigDecimal interestRate, BigDecimal minimumBalance, String secretKey ) {
        super(balance, primaryOwner, secondaryOwner, creationDate, status);
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        setSecretKey(secretKey);
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }


    //Savings with an interest rate other than the default, with a maximum interest rate of 0.5.
    public void setInterestRate(BigDecimal interestRate) {
       if (interestRate.compareTo(new BigDecimal(0.5)) >= 0){
           this.interestRate = interestRate;
       } else {
           System.err.println("Interest rate should be higher than 0.0025 but lower than 0.5");
           this.interestRate = new BigDecimal(0.5);
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    //Add interest yearly

    public void addInterest() {
//        if (Period.between(lastInterest, LocalDate.now()).getYears() >= 1) {
//            setBalance(getBalance().multiply(getInterestRate())
//                    .multiply(BigDecimal.valueOf(Period.between(lastInterest, LocalDate.now()).getYears())).add(getBalance()));
//            lastInterest.plusYears(Period.between(lastInterest, LocalDate.now()).getYears());
//
//        }

        if (LocalDate.now().getYear() > this.getLastInterest().getYear()) {
            for (int i = LocalDate.now().getYear(); i > this.getLastInterest().getYear() ; i--) {
                this.setBalance(this.getBalance().multiply(interestRate.add(BigDecimal.valueOf(1))));
            }
            setLastInterest(LocalDate.now());
        }
    }

    @Override
    public  void setBalance (BigDecimal balance){
        addInterest();
        if (balance.compareTo(BigDecimal.valueOf(250)) < 0) {
            super.setBalance(balance = minimumBalance.subtract(BigDecimal.valueOf(40)));
        } else{
            super.setBalance(balance);
        }
    }

    @Override
    public BigDecimal getBalance(){
        addInterest();
        return super.getBalance();
    }

    public LocalDate getLastInterest() {
        return lastInterest;
    }

    public void setLastInterest(LocalDate lastInterest) {
        this.lastInterest = lastInterest;
    }
}
