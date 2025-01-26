package com.example.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;
import java.util.*;

@Service 
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    /**
     * Registers a new user if the username is unique and the password is valid.
     * 
     * @param account The account object containing the username and password.
     * @return
     * @throw IllegalArgumentException If validation fails.
     * @throws DataIntegrityViolationException If the username is already taken.
     */
    public Account registerUser(Account account){
        // Validat username
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }

        // Validate Password
        if (account.getPassword() == null || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Password mnust be at least 4 characters long.");
        }

        // Check for duplicate username
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists.");
        }
        return accountRepository.save(account);
    }

    /**
     * Validates the login credentails.
     * 
     * @param username The username provided in the login request.
     * @param password The password provided in the login request.
     * 
     */
     public Account login(String username, String password) {

        // Validate username and password
        if (username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("Usename cannot be blank.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        // Check if the username and password match an account
        Optional<Account> accountOptional = accountRepository.findByUsernameAndPassword(username, password);

        if (accountOptional.isPresent()){
            return accountOptional.get();
        } else {
            throw new UnauthorizedException("Invalid username or password.");
        }
     }
}
