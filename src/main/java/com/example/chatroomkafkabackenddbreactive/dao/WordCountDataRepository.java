package com.example.chatroomkafkabackenddbreactive.dao;

import com.example.chatroomkafkabackenddbreactive.pojo.WordCountData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordCountDataRepository extends JpaRepository<WordCountData, String> {
}
