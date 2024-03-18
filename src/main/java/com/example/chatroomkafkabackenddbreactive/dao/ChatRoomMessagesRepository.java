package com.example.chatroomkafkabackenddbreactive.dao;

import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMessagesRepository extends JpaRepository<ChatRoomMessage, String> {
}
