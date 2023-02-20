package com.ironhack.FinalProjectBackend;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.FinalProjectBackend.dtos.AccountDTO;
import com.ironhack.FinalProjectBackend.dtos.UserDTO;
import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import com.ironhack.FinalProjectBackend.models.User.Address;

import com.ironhack.FinalProjectBackend.models.User.Admin;
import com.ironhack.FinalProjectBackend.models.User.User;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Checking;
import com.ironhack.FinalProjectBackend.models.bankAccounts.CreditCard;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Savings;
import com.ironhack.FinalProjectBackend.models.bankAccounts.StudentChecking;

import com.ironhack.FinalProjectBackend.models.enums.Status;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.*;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AccountHolderRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AdminRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.RoleRepository;

import com.ironhack.FinalProjectBackend.repositories.userRepositories.UserRepository;
import com.ironhack.FinalProjectBackend.services.impl.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
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

    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;
    AccountHolder accountHolder;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        accountHolder = new AccountHolder("Chris Evans", "CaptainAmerica", passwordEncoder.encode("123456"),
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980"));

        accountHolderRepository.save(accountHolder);

        userService.addRoleToUser("CaptainAmerica", "ROLE_USER");


    }

    @AfterEach
    void tearDown(){

      //  accountHolderRepository.deleteAll();
    }

    @Test
    void create_checkingAccount() throws Exception {

//
        AccountDTO checkingDTO = new AccountDTO(new BigDecimal(2000), accountHolder.getId(),
                null, LocalDate.now(), null,null,
                "AGSD123", null, null, null, ACTIVE );

        //convertir a json

        String body = objectMapper.writeValueAsString(checkingDTO);
        MvcResult result = mockMvc.perform(post("/new-checking").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Chris Evans"));

    }

    @Test
    void create_savingsAccount() throws Exception {

        AccountDTO savingsDTO = new AccountDTO(new BigDecimal(2000), accountHolder.getId(),
                null, LocalDate.now(), new BigDecimal(100),null,
                "AGSD123", null, null, new BigDecimal(0.2), ACTIVE );

        //convertir a json

        String body = objectMapper.writeValueAsString(savingsDTO);
        MvcResult result = mockMvc.perform(post("/new-savings").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();


        assertTrue(result.getResponse().getContentAsString().contains("Chris Evans"));

    }



    @Test

    void create_creditCard() throws Exception {

        AccountDTO creditCardDTO = new AccountDTO(new BigDecimal(2000), accountHolder.getId(),
                null, LocalDate.now(), null,null,
                null, null, new BigDecimal(5000), new BigDecimal(0.15), null );

        //convertir a json

        String body = objectMapper.writeValueAsString(creditCardDTO);
        MvcResult result = mockMvc.perform(post( "/new-creditcard").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

     assertTrue(result.getResponse().getContentAsString().contains("Chris Evans"));

    }



    @Test

    void accessBalance_usingAccountId() throws Exception {


        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder, null,
                LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));

        //convertir a json



        MvcResult result = mockMvc.perform(get("/balance/?accountId=" + checking.getAccountId())).andExpect(status().isOk()).andReturn();


       assertTrue(result.getResponse().getContentAsString().contains(BigDecimal.valueOf(5000).toString()));

    }

    @Test
    void modifyBalance_usingAccountId() throws Exception {


        Checking checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder, null,
                LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));

        //convertir a json


        MvcResult result = mockMvc.perform(patch("/balance/modify?accountId=" + checking.getAccountId() + "&newBalance=2000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(BigDecimal.valueOf(2000).toString()));
    }


@Test
    void createAdmin_user() throws Exception {
    UserDTO adminDTO = new UserDTO("Gal gadot", "Wonderwoman", "123456");
    //convertir a json

    String body = objectMapper.writeValueAsString(adminDTO);
    MvcResult result = mockMvc.perform(post( "/create-admin").content(body)
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

    assertTrue(result.getResponse().getContentAsString().contains("Wonderwoman"));
}

@Test
    void create_accountHolder() throws Exception {
    UserDTO accountHolderDTO = new UserDTO("Gal gadot", "Wonderwoman", "123456");
    //convertir a json

    String body = objectMapper.writeValueAsString(accountHolderDTO);
    MvcResult result = mockMvc.perform(post( "/create-accountholder").content(body)
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

    assertTrue(result.getResponse().getContentAsString().contains("Wonderwoman"));
}

@Test
    void create_thirdParty() throws Exception {
    UserDTO thirdParty = new UserDTO("Gal gadot", "Wonderwoman", "123456");
    //convertir a json

    String body = objectMapper.writeValueAsString(thirdParty);
    MvcResult result = mockMvc.perform(post( "/create-thirdparty").content(body)
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

    assertTrue(result.getResponse().getContentAsString().contains("Wonderwoman"));

}




}
