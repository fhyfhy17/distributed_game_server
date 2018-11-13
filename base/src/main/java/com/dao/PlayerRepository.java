package com.dao;

import com.entry.PlayerEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PlayerRepository extends MongoRepository<PlayerEntry, String> {

    List<PlayerEntry> findByName(String name);
}