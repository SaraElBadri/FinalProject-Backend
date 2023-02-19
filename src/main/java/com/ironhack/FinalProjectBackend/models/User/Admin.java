package com.ironhack.FinalProjectBackend.models.User;


import jakarta.persistence.Entity;

import java.util.Collection;

@Entity
public class Admin extends User {

    public Admin() {
    }

    public Admin( String name,  String username, String password) {
        super( name, username, password);

    }
}
