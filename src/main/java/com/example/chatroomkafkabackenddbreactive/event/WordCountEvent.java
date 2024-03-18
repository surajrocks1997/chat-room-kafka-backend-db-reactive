package com.example.chatroomkafkabackenddbreactive.event;

import com.example.chatroomkafkabackenddbreactive.pojo.WordCount;

public record WordCountEvent(WordCount wordCount) {
}
