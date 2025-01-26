package com.example.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.exception.UnauthorizedException;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    private final AccountService accountService;

    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }



    /**
     * Handles the POST /register endpoint for user registration.
     * 
     * @param account The account object containing username and password
     * @return A ResponseEntity with the registered Account or an error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account){
        try {
            Account createdAccount = accountService.registerUser(account);
            return ResponseEntity.status(HttpStatus.OK).body(createdAccount);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     *  Handles the POST /login endpoint for the user login
     * @param account The Account object containing username and password
     * @return A ResponseEntity with the authenticated Account or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        try {
            Account authenticateAccount = accountService.login(account.getUsername(), account.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(authenticateAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
