package com.ironhack.FinalProjectBackend.services.impl;



import ch.qos.logback.core.encoder.EchoEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.FinalProjectBackend.dtos.AccountDTO;
import com.ironhack.FinalProjectBackend.dtos.UserDTO;
import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.User.Admin;
import com.ironhack.FinalProjectBackend.models.User.ThirdParty;
import com.ironhack.FinalProjectBackend.models.User.User;
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

    @Autowired
    UserService userService;


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
            System.err.println(primaryOwner);
            return creditCardRepository.save(creditCard);

        }
        throw new IllegalArgumentException("This Id doesn't match any account Holder.");

    }

    //Admins should be able to access the balance for any account and to modify it.


    //ONLY ACCESS
    public BigDecimal accessBalance(Long accountId){

            Account selectedAcc = accountRepository.findById(accountId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account you are looking for doesn't exist in the database"));
            return selectedAcc.getBalance();

    }

    //ACCESS AND MODIFY

    public BigDecimal modifyBalance(Long accountId, BigDecimal newBalance){

            Account selectedAcc = accountRepository.findById(accountId).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "The account you are looking for doesn't exist in the database"));
            selectedAcc.setBalance(newBalance);
            accountRepository.save(selectedAcc);
            return selectedAcc.getBalance();


    }



    //Delete Account

    public void deleteAccount ( Long accountId){
        if (accountRepository.findById(accountId).isPresent()){
            accountRepository.deleteById(accountId);
        }
        throw new IllegalArgumentException("This Account Id doesn't match any account .");
    }

    //Create admin user

    public Admin createAdmin(UserDTO admin){

        Admin newAdmin =new Admin(admin.getName(), admin.getUsername(), admin.getPassword());
        userService.saveUser(newAdmin);
        userService.addRoleToUser(newAdmin.getUsername(), "ROLE_ADMIN");

        return newAdmin;
    }

    public AccountHolder createAccountHolder(UserDTO accountHolderDTO){
        AccountHolder accountholder = new AccountHolder(accountHolderDTO.getName(), accountHolderDTO.getUsername(), accountHolderDTO.getPassword(), LocalDate.now(), null, null);
        userService.saveUser(accountholder);
        userService.addRoleToUser(accountholder.getUsername(), "ROLE_USER");

        return accountholder;
    }

    public ThirdParty createThirdParty(UserDTO thirdPartyDTO){
        ThirdParty thirdParty = new ThirdParty(thirdPartyDTO.getName(), thirdPartyDTO.getUsername(), thirdPartyDTO.getPassword(), null);
        userService.saveUser(thirdParty);
        userService.addRoleToUser(thirdParty.getUsername(), "ROLE_THIRDPARTY");
        return thirdParty;
    }
}
