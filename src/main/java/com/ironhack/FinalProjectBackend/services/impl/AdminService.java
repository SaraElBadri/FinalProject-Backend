package com.ironhack.FinalProjectBackend.services.impl;



import com.ironhack.FinalProjectBackend.dtos.AccountDTO;
import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.bankAccounts.*;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.*;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AccountHolderRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
    @Autowired
    private SavingsRepository savingsRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;


    //Checking account creation
    public Account createCheckingAccount(AccountDTO newChecking) {

        if (accountHolderRepository.findById(newChecking.getPrimaryOwner()).isPresent()) {
            AccountHolder primaryOwner = accountHolderRepository.findById(newChecking.getPrimaryOwner()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account holder you are looking for doesn't exist in the database"));
            AccountHolder secondaryOwner = null;
            if (newChecking.getSecondaryOwner() != null && accountHolderRepository.findById(newChecking.getSecondaryOwner()).isPresent()) {
                secondaryOwner = accountHolderRepository.findById(newChecking.getSecondaryOwner()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "The account holder you are looking for doesn't exist in the database"));
            }

            StudentChecking studentAcc;
            Integer age = Period.between(primaryOwner.getDateOfBirth(), LocalDate.now()).getYears();

            if (age > 24) {
                Checking checkingAcc = new Checking(newChecking.getBalance(), primaryOwner, secondaryOwner,
                        newChecking.getCreationDate(), newChecking.getStatus(), newChecking.getSecretKey());

                return checkingRepository.save(checkingAcc);
            } else {
                System.err.println("Customer must be older than 24 years to create a checking account. For this customer, create a student Checking account.");

                studentAcc = new StudentChecking(newChecking.getBalance(), primaryOwner, secondaryOwner,
                        newChecking.getCreationDate(), newChecking.getStatus(), newChecking.getSecretKey());

            }
            return studentCheckingRepository.save(studentAcc);
        }
        throw new IllegalArgumentException("This Id doesn't match any account Holder.");

    }


    //Savings account creation

    public Savings createSavingsAccount(AccountDTO newSaving) {

        if (accountHolderRepository.findById(newSaving.getPrimaryOwner()).isPresent()) {
            AccountHolder primaryOwner = accountHolderRepository.findById(newSaving.getPrimaryOwner()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account holder you are looking for doesn't exist in the database"));
            AccountHolder secondaryOwner = null;
            if (newSaving.getSecondaryOwner() != null && accountHolderRepository.findById(newSaving.getSecondaryOwner()).isPresent()) {
                secondaryOwner = accountHolderRepository.findById(newSaving.getSecondaryOwner()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "The account holder you are looking for doesn't exist in the database"));
            }

            Savings savingAcc = new Savings(newSaving.getBalance(), primaryOwner, secondaryOwner, newSaving.getCreationDate(),
                    newSaving.getStatus(), newSaving.getInterestRate(), newSaving.getMinimumBalance(), newSaving.getSecretKey());
            return savingsRepository.save(savingAcc);


        }
        throw new IllegalArgumentException("This Id doesn't match any account Holder.");

    }


    //CreditCard creation

    public CreditCard createCreditCard( AccountDTO newCard) {

        if (accountHolderRepository.findById(newCard.getPrimaryOwner()).isPresent()) {
            AccountHolder primaryOwner = accountHolderRepository.findById(newCard.getPrimaryOwner()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account holder you are looking for doesn't exist in the database"));
            AccountHolder secondaryOwner = null;
            if (newCard.getSecondaryOwner() != null && accountHolderRepository.findById(newCard.getSecondaryOwner()).isPresent()) {
                secondaryOwner = accountHolderRepository.findById(newCard.getSecondaryOwner()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "The account holder you are looking for doesn't exist in the database"));
            }
            CreditCard creditCard = new CreditCard(newCard.getBalance(), primaryOwner, secondaryOwner,
                    newCard.getCreationDate(), newCard.getCreditLimit(), newCard.getInterestRate());

            return creditCardRepository.save(creditCard);

        }
        throw new IllegalArgumentException("This Id doesn't match any account Holder.");

    }

    //Admins should be able to access the balance for any account and to modify it.


    //ONLY ACCESS
    public BigDecimal accessBalance(Long accountId){
        if (accountRepository.findById(accountId).isPresent()){
            Account selectedAcc = accountRepository.findById(accountId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account you are looking for doesn't exist in the database"));
            return selectedAcc.getBalance();
        }
        throw new IllegalArgumentException("This Account Id doesn't match any account.");
    }

    //ACCESS AND MODIFY

    public BigDecimal modifyBalance(Long accountId, BigDecimal newBalance){
        if (accountRepository.findById(accountId).isPresent()){
            Account selectedAcc = accountRepository.findById(accountId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account you are looking for doesn't exist in the database"));
            selectedAcc.setBalance(newBalance);
            accountRepository.save(selectedAcc);
            return selectedAcc.getBalance();
        }
        throw new IllegalArgumentException("This Account Id doesn't match any account .");
    }



    //Delete Account

    public void deleteAccount ( Long accountId){
        if (accountRepository.findById(accountId).isPresent()){
            accountRepository.deleteById(accountId);
        }
        throw new IllegalArgumentException("This Account Id doesn't match any account .");
    }
}
