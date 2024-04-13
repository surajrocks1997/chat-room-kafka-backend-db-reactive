package com.example.chatroomkafkabackenddbreactive.controller;

import com.example.chatroomkafkabackenddbreactive.event.DataType;
import com.example.chatroomkafkabackenddbreactive.event.EventDataWrapper;
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

    private final Sinks.Many<EventDataWrapper<?>> sinkData = Sinks.many().multicast().directAllOrNothing();

    @EventListener
    public void onChangeData(EventDataWrapper<?> event) {
        log.info("from On Change Data Event");
        sinkData.tryEmitNext(event);
    }

    @GetMapping(value = "/insightData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<?>> getMessageEvent() {
        log.info("from On Change Chat Room Message Method");
        return sinkData.asFlux().map(event -> {
            if (event.getDataType() == DataType.CHAT_ROOM_AGG_DATA)
                return ServerSentEvent.<ChatRoomData>builder()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .event(String.valueOf(event.getDataType()))
                        .data((ChatRoomData) event.getData())
                        .retry(Duration.ofMillis(200))
                        .build();

            else if (event.getDataType() == DataType.WORD_COUNT_AGG_DATA)
                return ServerSentEvent.<WordCountData>builder()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .event(String.valueOf(event.getDataType()))
                        .data((WordCountData) event.getData())
                        .retry(Duration.ofMillis(200))
                        .build();


            else return null;


        });
    }
}
