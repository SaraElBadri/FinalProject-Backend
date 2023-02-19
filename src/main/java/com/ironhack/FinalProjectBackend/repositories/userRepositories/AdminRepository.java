package com.ironhack.FinalProjectBackend.repositories.userRepositories;


import com.ironhack.FinalProjectBackend.models.User.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
