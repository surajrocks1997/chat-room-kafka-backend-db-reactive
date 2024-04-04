package com.example.chatroomkafkabackenddbreactive.controller;

import com.example.chatroomkafkabackenddbreactive.dao.ChatRoomDataRepository;
import com.example.chatroomkafkabackenddbreactive.dao.WordCountDataRepository;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomData;
import com.example.chatroomkafkabackenddbreactive.pojo.WordCountData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@RestController
@RequestMapping("/init")
@CrossOrigin(CorsConfiguration.ALL)
@RequiredArgsConstructor
public class InitController {

    private final ChatRoomDataRepository chatRoomDataRepository;
    private final WordCountDataRepository wordCountDataRepository;

    @GetMapping("/fetchMessageData")
    public ResponseEntity<List<ChatRoomData>> getMessageDataFromDB() {
        List<ChatRoomData> allData = chatRoomDataRepository.findAll();
        return new ResponseEntity<>(allData, HttpStatus.OK);
    }

    @GetMapping("/fetchWordData")
    public ResponseEntity<List<WordCountData>> getWordCountDataFromDB() {
        List<WordCountData> topTwentyWordCountData = wordCountDataRepository.findTopWordCounts(PageRequest.of(0, 20));
        return new ResponseEntity<>(topTwentyWordCountData, HttpStatus.OK);
    }
}
