package com.ironhack.FinalProjectBackend.models.User;


import jakarta.persistence.Entity;

import java.util.Collection;

@Entity
public class ThirdParty extends User{

    private String hashedKey;

    public ThirdParty() {
    }

    public ThirdParty( String name, String username, String password, String hashedKey) {
        super( name, username, password);
        this.hashedKey = hashedKey;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
