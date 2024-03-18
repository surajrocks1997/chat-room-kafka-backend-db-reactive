package com.example.chatroomkafkabackenddbreactive.service;

import com.example.chatroomkafkabackenddbreactive.dao.ChatRoomMessagesRepository;
import com.example.chatroomkafkabackenddbreactive.event.ChatRoomMessageEvent;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "chat-room-aggregator-messages-per-room", groupId = "my-consumer-group", containerFactory = "messageListenerContainerKafkaListenerContainerFactory")
public class KafkaConsumerService_MessagePerRoom {

    private final ApplicationEventPublisher eventPublisher;
    private final ChatRoomMessagesRepository chatRoomMessagesRepository;

    @KafkaHandler
    public void consumeChatRoomMessages(ConsumerRecord<String, Long> consumerRecord) {

        ChatRoomMessage currentChatRoom = chatRoomMessagesRepository.findById(consumerRecord.key()).orElse(new ChatRoomMessage(consumerRecord.key(), 0L));
        currentChatRoom.setCount(currentChatRoom.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        ChatRoomMessage finalChatRoom = chatRoomMessagesRepository.save(currentChatRoom);

        eventPublisher.publishEvent(new ChatRoomMessageEvent(finalChatRoom));
    }

    @KafkaHandler(isDefault = true)
    public void handleMessage(ConsumerRecord<String, Object> consumerRecord) {
        ChatRoomMessage currentChatRoom = chatRoomMessagesRepository.findById(consumerRecord.key()).orElse(new ChatRoomMessage(consumerRecord.key(), 0L));
        currentChatRoom.setCount(currentChatRoom.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        ChatRoomMessage finalChatRoom = chatRoomMessagesRepository.save(currentChatRoom);

        eventPublisher.publishEvent(new ChatRoomMessageEvent(finalChatRoom));
    }

}
