package com.ironhack.FinalProjectBackend.services.impl;

import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.AccountRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;


}
