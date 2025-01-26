package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }


    /**
     * Creates a new message.
     * 
     * @param message The Message object to be created.
     * @return The create Message object with its auto-generated messageId.
     * @throws IllegalArgumentException If validation fails.
     */
    public Message createMessage(Message message){

        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank");
        }
        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters");
        }

        // Validate postedBy
        if (!accountRepository.findById(message.getPostedBy()).isPresent()) {
            throw new IllegalArgumentException("The user with the given ID does not exist");
        }

        // Set the timePostedEpoch
        // message.setTimePostedEpoch(System.currentTimeMillis() / 1000); // in seconds

        // Save and return the message
        return messageRepository.save(message);
    }


    /**
     * Retrieves all messages from the database
     * @return A list of all Message objects, or and empty list if no messages exist.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message by its ID.
     * @param messageId Tje OD pf the message to retrieve.
     * @return An Optional containing the Message if found, or empty if not.
     */
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    public int deleteMessagesById(Integer messageId) {

        // Check if the message exists
        boolean exists = messageRepository.existsById(messageId);

        if (exists) {
            messageRepository.deleteById(messageId);
            return 1; // Indicate that a message was deleted
        }

        return 0; // Indicate that no message was found
    }

    /**
     * Updates the text of a specific message identified by its ID 
     * @param messageId The ID of the message to update.
     * @param messageText The new text for the message.
     * @return The number of rows updated (1 if successful).
     * @throws IllegalArgumentException If the message text is blank, exceed 255 characts,
     *                                  the message does not exists, or the update fails.
     */
    public int updateMessageText(Integer messageId, String messageText){

        // Validate messageText
        if (messageText == null || messageText.trim().isEmpty()){
            throw new IllegalArgumentException("Nessage text cannot be blank");
        }
        if(messageText.length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }

        // Check if the message exists
        if (!messageRepository.existsById(messageId)) {
            throw new IllegalArgumentException("Message with ID " + messageId + " does not exist");
        }

        //update the message text
        int rowsUpdated = messageRepository.updateMessageTextById(messageId, messageText);

        // confirm update success
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Failed to update message text.");
        }
        return rowsUpdated;
    }

    /**
     * Retrieves all messages posted by a specific user.
     * 
     * @param accountId The ID of the user.
     * @return A list of Message objects posted by the user
     */
    public List<Message> getMessagesByUser(Integer accountId){
        return messageRepository.findByPostedBy(accountId);
    }

}
