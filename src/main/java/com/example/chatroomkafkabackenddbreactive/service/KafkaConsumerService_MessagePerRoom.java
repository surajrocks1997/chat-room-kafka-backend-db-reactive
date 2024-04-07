package com.example.chatroomkafkabackenddbreactive.service;

import com.example.chatroomkafkabackenddbreactive.dao.ChatRoomDataRepository;
import com.example.chatroomkafkabackenddbreactive.event.DataType;
import com.example.chatroomkafkabackenddbreactive.event.EventDataWrapper;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomData;
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
    private final ChatRoomDataRepository chatRoomDataRepository;

    @KafkaHandler
    public void consumeChatRoomMessages(ConsumerRecord<String, Long> consumerRecord) {

        ChatRoomData currentChatRoom = chatRoomDataRepository.findById(consumerRecord.key()).orElse(new ChatRoomData(consumerRecord.key(), 0L));
        currentChatRoom.setCount(currentChatRoom.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        ChatRoomData finalChatRoom = chatRoomDataRepository.save(currentChatRoom);

        eventPublisher.publishEvent(new EventDataWrapper<ChatRoomData>(DataType.CHAT_ROOM_AGG_DATA, finalChatRoom));
    }

    @KafkaHandler(isDefault = true)
    public void handleMessage(ConsumerRecord<String, Object> consumerRecord) {
        ChatRoomData currentChatRoom = chatRoomDataRepository.findById(consumerRecord.key()).orElse(new ChatRoomData(consumerRecord.key(), 0L));
        currentChatRoom.setCount(currentChatRoom.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        ChatRoomData finalChatRoom = chatRoomDataRepository.save(currentChatRoom);

        eventPublisher.publishEvent(new EventDataWrapper<ChatRoomData>(DataType.CHAT_ROOM_AGG_DATA, finalChatRoom));
    }

}
