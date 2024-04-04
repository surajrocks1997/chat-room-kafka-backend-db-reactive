package com.example.chatroomkafkabackenddbreactive.service;

import com.example.chatroomkafkabackenddbreactive.dao.WordCountDataRepository;
import com.example.chatroomkafkabackenddbreactive.event.WordCountDataEvent;
import com.example.chatroomkafkabackenddbreactive.pojo.WordCountData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "chat-room-aggregator-word-count", groupId = "my-consumer-group", containerFactory = "messageListenerContainerKafkaListenerContainerFactory")
public class KafkaConsumerService_WordCount {

    private final ApplicationEventPublisher eventPublisher;
    private final WordCountDataRepository wordCountDataRepository;

    @KafkaHandler
    public void consumerWordCount(ConsumerRecord<String, Long> consumerRecord) {
        WordCountData currentWordCountData = wordCountDataRepository.findById(consumerRecord.key()).orElse(new WordCountData(consumerRecord.key(), 0L));
        currentWordCountData.setCount(currentWordCountData.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        WordCountData finalWordCountData = wordCountDataRepository.save(currentWordCountData);

        eventPublisher.publishEvent(new WordCountDataEvent(finalWordCountData));
    }

    @KafkaHandler(isDefault = true)
    public void handleMessage(ConsumerRecord<String, Object> consumerRecord) {
        WordCountData currentWordCountData = wordCountDataRepository.findById(consumerRecord.key()).orElse(new WordCountData(consumerRecord.key(), 0L));
        currentWordCountData.setCount(currentWordCountData.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        WordCountData finalWordCountData = wordCountDataRepository.save(currentWordCountData);

        eventPublisher.publishEvent(new WordCountDataEvent(finalWordCountData));
    }

}
