package com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories;

import com.ironhack.FinalProjectBackend.models.bankAccounts.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
