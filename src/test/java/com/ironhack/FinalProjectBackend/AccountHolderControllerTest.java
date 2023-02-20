package com.ironhack.FinalProjectBackend;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountHolderControllerTest {



    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper= new ObjectMapper().registerModule(new JavaTimeModule());;
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

    AccountHolder accountHolder;
    AccountHolder accountHolder2;
    Checking checking;
    StudentChecking studentChecking;
    CreditCard creditCard;
    Savings saving;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        accountHolder = new AccountHolder("Chris Evans", "CaptainAmerica", passwordEncoder.encode("123456"),
                LocalDate.of(1975, 2, 12),
                new Address("Dalt", "9", "08980"),
                new Address("Dalt", "9", "08980"));

        accountHolderRepository.save(accountHolder);

        userService.addRoleToUser("CaptainAmerica", "ROLE_USER");

        accountHolder2 = new AccountHolder("Robert Downey", "IronMan", "456789",
                LocalDate.of(1965, 8, 07),
                new Address("NYC", "2", "00011"),
                new Address("NYC", "2", "00011"));

        accountHolderRepository.save(accountHolder2);

        userService.addRoleToUser("IronMan", "ROLE_USER");



        saving = savingsRepository.save(new Savings(new BigDecimal(2000), accountHolder, accountHolder2, LocalDate.of(2022, 05, 20),
                ACTIVE, new BigDecimal(0.2), new BigDecimal(100), "AGSD123"));
         checking = checkingRepository.save(new Checking(new BigDecimal(5000), accountHolder2, null, LocalDate.of(2023, 01, 01), ACTIVE, "GHD789"));
         studentChecking = studentCheckingRepository.save(new StudentChecking(new BigDecimal(1500),accountHolder, accountHolder2, LocalDate.of(2019, 02, 15), ACTIVE, "ERT456" ));
         creditCard = creditCardRepository.save(new CreditCard(new BigDecimal(1000), accountHolder,accountHolder2, LocalDate.of(2022, 05,20), new BigDecimal(5000) ,new BigDecimal(0.15)));

        accountRepository.saveAll(List.of(saving, checking, creditCard, studentChecking));

    }

    @AfterEach
    void tearDown(){
//        accountRepository.deleteAll();
//        accountHolderRepository.deleteAll();
    }



    @Test
    void transferMoney_fromOneAccountToAnother() throws Exception {



        //convertir a json



        String body = objectMapper.writeValueAsString(saving);
        MvcResult result = mockMvc.perform(put("/transfer/?senderId="+ saving.getAccountId() +"&receiverId="+checking.getAccountId()+"&money=500").content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal(5500), checking.getBalance());
        assertEquals(new BigDecimal(1500), saving.getBalance());

    }

}
