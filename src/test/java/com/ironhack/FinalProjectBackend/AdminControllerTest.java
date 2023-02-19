package com.ironhack.FinalProjectBackend;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.User.Address;

import com.ironhack.FinalProjectBackend.models.bankAccounts.Checking;
import com.ironhack.FinalProjectBackend.models.bankAccounts.CreditCard;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Savings;
import com.ironhack.FinalProjectBackend.models.bankAccounts.StudentChecking;

import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.*;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AccountHolderRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AdminRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.RoleRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.ironhack.FinalProjectBackend.models.enums.Status.ACTIVE;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class AdminControllerTest {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    @Autowired
    private SavingsRepository savingsRepository;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));
        AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("Robert Downey", "IronMan", "456789",
                LocalDate.of(1965, 8, 07),
                new Address("NYC", "2", "00011"),
                new Address("NYC", "2", "00011")));
        AccountHolder accountHolder3 = accountHolderRepository.save(new AccountHolder("Chris Hemsworth", "Thor", "147258",
                LocalDate.of(1983, 03, 30),
                new Address("Lluna", "24", "08001"),
                new Address("Lluna", "24", "08001")));
        AccountHolder accountHolder4 = accountHolderRepository.save(new AccountHolder("Tom Holland", "SpiderMan", "456789",
                LocalDate.of(1999, 02, 27),
                new Address("Lluna", "24", "08001"),
                new Address("Lluna", "24", "08001")));

        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2, accountHolder3, accountHolder4));



        Savings saving = savingsRepository.save(new Savings(new BigDecimal(2000), accountHolder1, accountHolder2, LocalDate.of(2022, 05, 20),
                ACTIVE, new BigDecimal(0.2), new BigDecimal(100), "AGSD123"));
        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder3, null, LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));
        StudentChecking studentChecking = studentCheckingRepository.save(new StudentChecking(new BigDecimal(1500),accountHolder4, accountHolder2, LocalDate.of(2019, 02, 15), ACTIVE, "ERT456" ));
        CreditCard creditCard = creditCardRepository.save(new CreditCard(new BigDecimal(1000), accountHolder1,accountHolder2, LocalDate.of(2022, 05,20), new BigDecimal(5000) ,new BigDecimal(0.15)));

        accountRepository.saveAll(List.of(saving, checking, creditCard, studentChecking));

    }

    @AfterEach
    void tearDown(){
        accountHolderRepository.deleteAll();
    }

    @Test
    void create_checkingAccount() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));

        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder1, null,
                LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));

        //convertir a json

        String body = objectMapper.writeValueAsString(checking);
        MvcResult result = mockMvc.perform(post("/new-checking").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Chris Evans"));

    }

    @Test
    void create_savingsAccount() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));

        Savings saving = savingsRepository.save(new Savings(new BigDecimal(2000), accountHolder1, accountHolder1, LocalDate.of(2022, 05, 20),
                ACTIVE, new BigDecimal(0.2), new BigDecimal(100), "AGSD123"));

        //convertir a json

        String body = objectMapper.writeValueAsString(saving);
        MvcResult result = mockMvc.perform(post("/new-savings").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Chris Evans"));

    }



    @Test

    void create_creditCard() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));

        CreditCard creditCard = creditCardRepository.save(new CreditCard(new BigDecimal(1000), accountHolder1, null,
                LocalDate.of(2022, 05,20), new BigDecimal(5000) ,new BigDecimal(0.15)));

        //convertir a json

        String body = objectMapper.writeValueAsString(creditCard);
        MvcResult result = mockMvc.perform(post( "/new-creditcard").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Chris Evans"));

    }



    @Test

    void accessBalance_usingAccountId() throws Exception {
        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));

        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder1, null,
                LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));

        //convertir a json

        String body = objectMapper.writeValueAsString(checking);

        MvcResult result = mockMvc.perform(get("/balance/" + checking.getAccountId())).andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(BigDecimal.valueOf(5000).toString()));

    }

    @Test
    void modifyBalance_usingAccountId() throws Exception {
        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));

        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder1, null,
                LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));

        //convertir a json

        String body = objectMapper.writeValueAsString(checking);
        MvcResult result = mockMvc.perform(patch("/balance/modify/" + checking.getAccountId() + "newBalance=2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(BigDecimal.valueOf(7000).toString()));
    }







}
