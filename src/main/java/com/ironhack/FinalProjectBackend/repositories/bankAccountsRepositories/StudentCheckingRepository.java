package com.ironhack.FinalProjectBackend.repositories.bankAccountsRepositories;


import com.ironhack.FinalProjectBackend.models.bankAccounts.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Long> {

}
