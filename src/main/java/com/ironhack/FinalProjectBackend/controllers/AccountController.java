package com.ironhack.FinalProjectBackend.controllers;

import com.ironhack.FinalProjectBackend.services.impl.AccountHolderService;
import com.ironhack.FinalProjectBackend.services.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountHolderService accountHolderService;
}
