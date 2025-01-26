package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    // Custom query method to check if username exists
    Optional<Account> findByUsername(String username);

    // Find an account by username and password
    Optional<Account> findByUsernameAndPassword(String username, String password);
}
