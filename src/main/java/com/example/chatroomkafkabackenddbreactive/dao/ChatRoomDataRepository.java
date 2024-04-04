package com.example.chatroomkafkabackenddbreactive.dao;

import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomDataRepository extends JpaRepository<ChatRoomData, String> {
}
