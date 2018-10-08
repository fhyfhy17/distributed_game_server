package com.service;

import com.annotation.EventListener;
import com.event.TestEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@EventListener
@Slf4j
public class TestService {

    @Subscribe
    public void test(TestEvent testEvent) {
        log.info("test = {}", testEvent.getTestWord());
    }
}
