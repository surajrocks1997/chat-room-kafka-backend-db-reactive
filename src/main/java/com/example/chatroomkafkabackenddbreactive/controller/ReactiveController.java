package com.example.chatroomkafkabackenddbreactive.controller;

import com.example.chatroomkafkabackenddbreactive.event.ChatRoomDataEvent;
import com.example.chatroomkafkabackenddbreactive.event.WordCountDataEvent;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomData;
import com.example.chatroomkafkabackenddbreactive.pojo.WordCountData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
@CrossOrigin(CorsConfiguration.ALL)
@Slf4j
public class ReactiveController {

    private final Sinks.Many<ChatRoomDataEvent> sinkMessageData = Sinks.many().multicast().directAllOrNothing();
    private final Sinks.Many<WordCountDataEvent> sinkWordCountData = Sinks.many().multicast().directAllOrNothing();

    @EventListener
    public void onChangeChatRoomMessage(ChatRoomDataEvent event) {
        log.info("from On Change Chat Room Message Event");
        sinkMessageData.tryEmitNext(event);
    }

    @EventListener
    public void onChangeWordCount(WordCountDataEvent event) {
        log.info("from On Word Count Event");
        sinkWordCountData.tryEmitNext(event);
    }

    @GetMapping(value = "/messageData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ChatRoomData>> getMessageEvent() {
        log.info("from On Change Chat Room Message Method");
        return sinkMessageData.asFlux().map(event -> ServerSentEvent.<ChatRoomData>builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .event("messageEvent")
                .data(event.chatRoomData())
                .retry(Duration.ofMillis(200))
                .build());

        //        return sinkMessageData.asFlux().map(
//                event -> ServerSentEvent.<ChatRoomMessage>builder(event.chatRoomMessage()).build());
    }

    @GetMapping(value = "/wordCountData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<WordCountData>> getWordCount() {
        log.info("from On Change Word Method");

        return sinkWordCountData.asFlux().map(event -> ServerSentEvent.<WordCountData>builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .event("wordCountEvent")
                .data(event.wordCountData())
                .retry(Duration.ofMillis(200))
                .build());

//        return sinkWordCountData.asFlux().map(
//                event -> ServerSentEvent.<WordCount>builder(event.wordCount()).build());
    }
}
