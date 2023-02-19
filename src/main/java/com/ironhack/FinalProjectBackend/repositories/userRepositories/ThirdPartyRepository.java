package com.ironhack.FinalProjectBackend.repositories.userRepositories;

import com.ironhack.FinalProjectBackend.models.User.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
    ThirdParty findByHashedKey(String hashedKey);
}
