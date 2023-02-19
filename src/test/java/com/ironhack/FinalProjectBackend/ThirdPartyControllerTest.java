package com.ironhack.FinalProjectBackend;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.User.Address;
import com.ironhack.FinalProjectBackend.models.User.ThirdParty;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Checking;
import com.ironhack.FinalProjectBackend.models.bankAccounts.CreditCard;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Savings;
import com.ironhack.FinalProjectBackend.models.bankAccounts.StudentChecking;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.AccountRepository;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.CheckingRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AccountHolderRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.ThirdPartyRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ThirdPartyControllerTest {

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        ThirdParty thirdParty = thirdPartyRepository.save(new ThirdParty("Tom Holland", "SpiderMan", "123456", "ASDFGHHJ"));



    }

    @AfterEach
    void tearDown(){
       thirdPartyRepository.deleteAll();
    }

    @Test

    void thirdParty_sendMoney () throws Exception {
        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));

        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder1, null,
                LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));

        ThirdParty thirdParty = thirdPartyRepository.save(new ThirdParty("Tom Holland", "SpiderMan", "123456", ""));

        //convertir a json

        String body = objectMapper.writeValueAsString(thirdParty);
        MvcResult result = mockMvc.perform(post("/transfer-send/ASDFGHHJ" + checking.getAccountId() +"secretKey=GHD789?money=2000").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal(7000), checking.getBalance());

    }


    @Test

    void thirdParty_receiveMoney () throws Exception {
        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980")));

        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder1, null,
                LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));

        ThirdParty thirdParty = thirdPartyRepository.save(new ThirdParty("Tom Holland", "SpiderMan", "123456", ""));

        //convertir a json

        String body = objectMapper.writeValueAsString(thirdParty);
        MvcResult result = mockMvc.perform(post("/transfer-receive/ASDFGHHJ" + checking.getAccountId() +"secretKey=GHD789?money=2000").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal(3000), checking.getBalance());


    }



}
