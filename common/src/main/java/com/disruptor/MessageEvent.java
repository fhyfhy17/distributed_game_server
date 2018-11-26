package com.disruptor;

import com.pojo.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageEvent extends BaseEvent {
    private Message message;
}
