package com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories;


import com.ironhack.FinalProjectBackend.models.bankAccounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
