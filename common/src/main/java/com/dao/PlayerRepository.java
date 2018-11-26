package com.dao;

import com.entry.PlayerEntry;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PlayerRepository extends PagingAndSortingRepository<PlayerEntry, String> {

    List<PlayerEntry> findPlayerEntityByName(String name);
}