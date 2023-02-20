package com.ironhack.FinalProjectBackend.controllers;


import com.ironhack.FinalProjectBackend.dtos.AccountDTO;
import com.ironhack.FinalProjectBackend.dtos.UserDTO;
import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.User.Admin;
import com.ironhack.FinalProjectBackend.models.User.ThirdParty;
import com.ironhack.FinalProjectBackend.models.User.User;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Account;
import com.ironhack.FinalProjectBackend.services.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping ("/new-checking")
    @ResponseStatus (HttpStatus.CREATED)
    public Account createCheckingAccount (@RequestBody AccountDTO newChecking){
        return adminService.createCheckingAccount(newChecking);
    }


    @PostMapping ("/new-savings")
    @ResponseStatus (HttpStatus.CREATED)
    public Account createSavingAccount (@RequestBody AccountDTO newSaving){
        return adminService.createSavingsAccount(newSaving);
    }

    @PostMapping ("/new-creditcard")
    @ResponseStatus (HttpStatus.CREATED)
    public Account createCreditCard (@RequestBody AccountDTO newCard){
        return adminService.createCreditCard(newCard);
    }

    @GetMapping("/balance/{accountId}")
    @ResponseStatus (HttpStatus.OK)
    public BigDecimal accessBalance (@PathVariable Long accountId){
        return adminService.accessBalance(accountId);
    }

    @PatchMapping("/balance/modify")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal modifyBalance ( @RequestParam Long accountId, @RequestParam BigDecimal newBalance){
        return adminService.modifyBalance(accountId, newBalance);
    }

    @DeleteMapping("/delete/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount (@PathVariable Long accountId){
        adminService.deleteAccount(accountId);
    }

    @PostMapping ("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createAdmin(@RequestBody UserDTO admin){
        return adminService.createAdmin(admin);
    }

    @PostMapping ("/create-accountholder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody UserDTO accountHolder){
        return adminService.createAccountHolder(accountHolder);
    }

    @PostMapping("/create-thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody UserDTO thirdParty){
        return adminService.createThirdParty(thirdParty);
    }


}
