package com.ironhack.FinalProjectBackend.controllers;


import com.ironhack.FinalProjectBackend.services.impl.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AccountHolderController {

    @Autowired
    AccountHolderService accountHolderService;

    @PutMapping ("/transfer/{senderId}/{receiverId}")
    @ResponseStatus(HttpStatus.OK)
    public void transferMoney(@PathVariable Long senderId, @PathVariable Long receiverId, @RequestParam BigDecimal money){
        accountHolderService.transferMoney(senderId, receiverId, money);
    }

    @GetMapping ("/balance/{userId}")
    @ResponseStatus (HttpStatus.OK)
    public BigDecimal checkBalance (@RequestParam Long accountId, @PathVariable Long userId){
        return accountHolderService.checkBalance(accountId, userId);
    }
}
