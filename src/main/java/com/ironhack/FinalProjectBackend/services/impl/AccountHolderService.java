package com.ironhack.FinalProjectBackend.services.impl;


import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Account;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.AccountRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AccountHolderRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class AccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;


    //transfer money from own account to another

    public void transferMoney (Long senderAccountId, Long receiverAccountId, BigDecimal money){
        if(accountRepository.findById(senderAccountId).isPresent()){
            Account account = accountRepository.findById(senderAccountId).get();
            if(account == null) throw new IllegalArgumentException("The account does not exist");
            if(account.getBalance().compareTo(money) < 0) throw new IllegalArgumentException("The account has not enough balance to proceed with the transaction");

            if(accountRepository.findById(receiverAccountId).isPresent()){
                Account account2 = accountRepository.findById(receiverAccountId).get();
                account2.setBalance(account2.getBalance().add(money));
                account.setBalance(account.getBalance().subtract(money));
                accountRepository.save (account);
                accountRepository.save(account2);
            }

            throw new IllegalArgumentException("Receiver account does not exist");
        }

        throw new IllegalArgumentException("Sender account does not exist");

    }

    //Access your own account balance

    public BigDecimal checkBalance(Long accountId, Long userId) {
        Account selectedAcc = null;
        if (accountHolderRepository.findById(userId).isPresent()) {
            AccountHolder accountHolder = accountHolderRepository.findById(userId).get();

            for (Account account : accountHolder.getPrimanyOwnershipList()) {
                if (account.getAccountId() == accountId) {
                    selectedAcc = accountRepository.findById(accountId).orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "This account doesn't exist"));

                } else {
                    throw new IllegalArgumentException("You can't access this account");
                }

            }
        } else {
            throw new IllegalArgumentException("This Account Id doesn't match any account .");
        }
        return selectedAcc.getBalance();
    }



}
