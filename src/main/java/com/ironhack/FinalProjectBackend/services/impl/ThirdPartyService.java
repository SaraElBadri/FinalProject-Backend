package com.ironhack.FinalProjectBackend.services.impl;


import com.ironhack.FinalProjectBackend.models.User.ThirdParty;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Account;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Checking;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Savings;
import com.ironhack.FinalProjectBackend.models.bankAccounts.StudentChecking;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.AccountRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    AccountRepository accountRepository;

    //Send money from third party

    public void sendMoney(String hashedKey, Long accountId, Long secretKey, BigDecimal money){

        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);

        Account account = accountRepository.findById(accountId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account you are looking for doesn't exist in the database"));

        if(account instanceof Checking){
            Checking checkingAccount = (Checking) account;
            if(!checkingAccount.getSecretKey().equals(secretKey)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secret key does not match");
        }

        if(account instanceof StudentChecking){
            StudentChecking studentAccount = (StudentChecking) account;
            if(!studentAccount.getSecretKey().equals(secretKey)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secret key does not match");
        }

        if(account instanceof Savings){
            Savings savingsAccount = (Savings) account;
            if(!savingsAccount.getSecretKey().equals(secretKey)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secret key does not match");
        }

        account.setBalance(account.getBalance().add(money));

    }

    // Receive money from regular account to a third party one



    public void receiveMoney (String hashedKey, Long accountId, Long secretKey, BigDecimal money){
        ThirdParty thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);
        Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "The account you are looking for doesn't exist in the database"));

        if(account instanceof Checking){
            Checking checkingAccount = (Checking) account;
            if(!checkingAccount.getSecretKey().equals(secretKey)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secret key does not match");
        }

        if(account instanceof StudentChecking){
            StudentChecking studentAccount = (StudentChecking) account;
            if(!studentAccount.getSecretKey().equals(secretKey)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secret key does not match");
        }

        if(account instanceof Savings){
            Savings savingsAccount = (Savings) account;
            if(!savingsAccount.getSecretKey().equals(secretKey)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secret key does not match");
        }

        account.setBalance(account.getBalance().subtract(money));

    }


}
