package com.example.chatroomkafkabackenddbreactive.dao;

import com.example.chatroomkafkabackenddbreactive.pojo.WordCountData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordCountDataRepository extends JpaRepository<WordCountData, String> {

    @Query("SELECT w from WordCountData w ORDER BY w.count DESC")
    List<WordCountData> findTopWordCounts(Pageable pageable);
}
