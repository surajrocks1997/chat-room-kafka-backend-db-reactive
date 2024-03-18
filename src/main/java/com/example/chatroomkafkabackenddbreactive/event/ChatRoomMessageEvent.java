package com.example.chatroomkafkabackenddbreactive.event;

import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomMessage;

public record ChatRoomMessageEvent(ChatRoomMessage chatRoomMessage) {
}
