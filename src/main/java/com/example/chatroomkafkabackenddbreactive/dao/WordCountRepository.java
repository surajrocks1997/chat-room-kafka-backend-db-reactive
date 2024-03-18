package com.example.chatroomkafkabackenddbreactive.dao;

import com.example.chatroomkafkabackenddbreactive.pojo.WordCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordCountRepository extends JpaRepository<WordCount, String> {
}
