package com.example.chatroomkafkabackenddbreactive.event;

import com.example.chatroomkafkabackenddbreactive.pojo.WordCountData;

public record WordCountDataEvent(WordCountData wordCountData) {
}
