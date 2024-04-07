package com.example.chatroomkafkabackenddbreactive.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDataWrapper<T> {
    private DataType dataType;
    private T data;
}
