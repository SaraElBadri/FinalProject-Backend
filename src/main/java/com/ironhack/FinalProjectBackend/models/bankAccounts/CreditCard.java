package com.ironhack.FinalProjectBackend.models.bankAccounts;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Entity
public class CreditCard extends Account {

    private BigDecimal creditLimit = BigDecimal.valueOf(100);
    private BigDecimal interestRate = BigDecimal.valueOf(0.2);

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastInterest = getCreationDate();

    public CreditCard() {
    }

    public CreditCard(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, LocalDate creationDate, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryOwner, secondaryOwner, creationDate);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
        setBalance(balance);
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }


    //creditLimit higher than 100 but not higher than 100000
    public void setCreditLimit(@NotNull BigDecimal creditLimit) {
        if(creditLimit.compareTo(BigDecimal.valueOf(100000)) <= 0 && creditLimit.compareTo(BigDecimal.valueOf(100)) >=0){
            this.creditLimit = creditLimit;
        } else {
            System.err.println("Credit Limit must be between 100 and 100000. Will be set by default to 100000. ");
            this.creditLimit = BigDecimal.valueOf(100000);
        }
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }


    //An interestRate less than 0.2 but not lower than 0.1
    public void setInterestRate(BigDecimal interestRate) {
        if(interestRate.compareTo(BigDecimal.valueOf(0.1)) < 0){
            this.interestRate = interestRate;
        } else {
            System.err.println("Interest rate must be between 0.1 and 0.2. Will be set by default to 0.1 ");
            this.interestRate = BigDecimal.valueOf(0.2);
        }
    }

    //Add interest per month
    public void addInterest(){
//        if(Period.between(lastInterest, LocalDate.now()).getMonths() >= 1){
//            setBalance(getBalance().multiply(getInterestRate().divide(BigDecimal.valueOf(12))
//                    .multiply(BigDecimal.valueOf(Period.between(lastInterest, LocalDate.now()).getMonths())).add(getBalance())));
//            lastInterest.plusMonths(Period.between(lastInterest, LocalDate.now()).getMonths());
//        }

        if (LocalDate.now().getMonthValue() > getLastInterest().getMonthValue() && LocalDate.now().getYear() >= getLastInterest().getYear()) {
            for (int i = LocalDate.now().getMonthValue(); i > getLastInterest().getMonthValue() ; i--) {
                this.setBalance(this.getBalance().multiply(interestRate.add(BigDecimal.valueOf(1))));
            }
            setLastInterest(LocalDate.now());
        }
    }


    public LocalDate getLastInterest() {
        return lastInterest;
    }

    public void setLastInterest(LocalDate lastInterest) {
        this.lastInterest = lastInterest;
    }

    @Override
    public BigDecimal getBalance(){
        addInterest();
        return super.getBalance();
    }

    @Override
    public void setBalance(BigDecimal balance){
        addInterest();
        super.setBalance(balance);
    }
}
