package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.messageText = :messageText WHERE m.messageId = :messageId")
    int updateMessageTextById(Integer messageId, String messageText);

    List<Message> findByPostedBy(Integer postedBy);

}
