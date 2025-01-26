package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.UnauthorizedException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
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

    /**
     * Handles the POST /messages endpoint for creating new messages.
     * 
     * @param message The Message object to be created
     * @return A ResponseEntity with the created Message or an error Message.
     */
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     * Handles the GET /messages endpoint
     * @return A ResponseEntity containing a list of all messages.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages); 
    }


    /**
     * Handles GET /messages/{messageId} endpoint
     * @param messageId The ID of the message to retrieve.
     * @return A ResponseEntity containing the message if found, or an empty response if not.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);

        // return the message if found, or an empty response if not
        return message.map(ResponseEntity::ok).orElse(ResponseEntity.ok().build());
    }



    /**
     * Handles the DELETE /message/{messageId} endpoint. 
     * 
     * @param messageId The ID of the message to delete.
     * @return A ResponseEntity with the number of rows deleted or an empty response body.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessagesById(messageId);

        if (rowsDeleted == 1) {
            // Return 1 if a message was deleted
            return ResponseEntity.ok(rowsDeleted);
        } else {
            // Return an empty response body if no message existed
            return ResponseEntity.ok().build();
        }
    }


    /**
     * Handles the PATCH /message/{messageId} endpoint
     * 
     * @param messageId The ID of the message to update
     * @param requestBody The request body containing the new messageText.
     * @return A ResponseEntity containing the number of rows updated or an error message.
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String, String> requestBody) {
        try {
            //Extract the new messageText from the request body
            String messageText = requestBody.get("messageText");

            // Call the service layer to update the message
            int rowsUpdate = messageService.updateMessageText(messageId, messageText);

            // If successful, return the number of rows updated
            return ResponseEntity.ok(rowsUpdate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // return 500 internal server error for unexpected exceptions
            return ResponseEntity.status(500).body("Internal sever errror: " + e.getMessage());
        }
    }

    /**
     * Handles the GET /accounts/{accountId}/messages endpoint.
     * @param accountId The ID of the user whose messages should be retrieved.
     * @return A ResponseEntity containing the list of messages or an empty list.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);
    }
}
