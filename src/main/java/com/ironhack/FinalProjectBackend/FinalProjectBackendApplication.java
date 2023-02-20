
package com.ironhack.FinalProjectBackend;

import com.ironhack.FinalProjectBackend.models.User.*;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Checking;
import com.ironhack.FinalProjectBackend.models.bankAccounts.CreditCard;
import com.ironhack.FinalProjectBackend.models.bankAccounts.Savings;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.AccountRepository;
import com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories.CheckingRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AccountHolderRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.AdminRepository;
import com.ironhack.FinalProjectBackend.repositories.userRepositories.ThirdPartyRepository;
import com.ironhack.FinalProjectBackend.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.time.LocalDate;

@SpringBootApplication
public class FinalProjectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectBackendApplication.class, args);
	}


	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Autowired
	AdminRepository adminRepository;
	@Autowired
	private CheckingRepository checkingRepository;
	@Autowired
	private ThirdPartyRepository thirdPartyRepository;


	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {



            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_THIRDPARTY"));

            userService.saveUser(new User(null, "John Doe", "john", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "James Smith", "james", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "Jane Carry", "jane", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "Chris Anderson", "chris", "1234", new ArrayList<>()));

            userService.addRoleToUser("john", "ROLE_USER");
            userService.addRoleToUser("james", "ROLE_ADMIN");
            userService.addRoleToUser("jane", "ROLE_USER");
            userService.addRoleToUser("chris", "ROLE_ADMIN");
            userService.addRoleToUser("chris", "ROLE_USER");

//			AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Chris Evans", "CaptainAmerica", "123456",
//					LocalDate.of(1975, 2, 12),
//					new Address("Dalt", "3", "08980"),
//					new Address("Dalt", "3", "08980")));
//			AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("Robert Downey", "IronMan", "456789",
//					LocalDate.of(1965, 8, 07),
//					new Address("NYC", "2", "00011"),
//					new Address("NYC", "2", "00011")));
//			AccountHolder accountHolder3 = accountHolderRepository.save(new AccountHolder("Chris Hemsworth", "Thor", "147258",
//					LocalDate.of(1983, 03, 30),
//					new Address("Lluna", "24", "08001"),
//					new Address("Lluna", "24", "08001")));









		};
	}

}
