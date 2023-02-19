package com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories;

import com.ironhack.FinalProjectBackend.models.bankAccounts.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository extends JpaRepository<Savings, Long> {
}
