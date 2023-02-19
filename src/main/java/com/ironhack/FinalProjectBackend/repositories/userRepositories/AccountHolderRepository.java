package com.ironhack.FinalProjectBackend.repositories.userRepositories;


import com.ironhack.FinalProjectBackend.models.User.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
    AccountHolder findByName(String sender);
}
