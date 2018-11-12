package com.service;

import com.annotation.EventListener;
import com.dao.AbsbaseRepository;
import com.dao.PlayerRepository;
import com.entry.PlayerEntry;
import com.event.TestEvent;
import com.google.common.eventbus.Subscribe;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
@EventListener
@Slf4j
@Data
public class TestService {
    @Autowired
    PlayerRepository playerRepository;


    @Subscribe
    public void test(TestEvent testEvent) {
        log.info("test = {}", testEvent.getTestWord());
    }
}
