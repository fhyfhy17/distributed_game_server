package com.dao;

import com.entry.PlayerEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PlayerRepository extends CrudRepository<PlayerEntry, Long> {

    List<PlayerEntry> findPlayerEntryByName(String name);
}