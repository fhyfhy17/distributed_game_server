package com.dao;

import com.entry.PlayerEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PlayerRepository extends CrudRepository<PlayerEntry, Long> {

}