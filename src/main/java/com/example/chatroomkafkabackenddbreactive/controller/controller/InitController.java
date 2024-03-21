package com.example.chatroomkafkabackenddbreactive.controller.controller;

import com.example.chatroomkafkabackenddbreactive.dao.ChatRoomMessagesRepository;
import com.example.chatroomkafkabackenddbreactive.dao.WordCountRepository;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomMessage;
import com.example.chatroomkafkabackenddbreactive.pojo.WordCount;
import lombok.RequiredArgsConstructor;
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

    private final ChatRoomMessagesRepository chatRoomMessagesRepository;
    private final WordCountRepository wordCountRepository;

    @GetMapping("/fetchMessageData")
    public ResponseEntity<List<ChatRoomMessage>> getMessageDataFromDB() {
        List<ChatRoomMessage> allData = chatRoomMessagesRepository.findAll();
        return new ResponseEntity<>(allData, HttpStatus.OK);
    }

    @GetMapping("/fetchWordData")
    public ResponseEntity<List<WordCount>> getWordCountDataFromDB() {
        List<WordCount> allData = wordCountRepository.findAll();
        return new ResponseEntity<>(allData, HttpStatus.OK);
    }
}
