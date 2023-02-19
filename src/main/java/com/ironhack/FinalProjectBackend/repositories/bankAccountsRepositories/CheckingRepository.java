package com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories;

import com.ironhack.FinalProjectBackend.models.bankAccounts.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {
}
