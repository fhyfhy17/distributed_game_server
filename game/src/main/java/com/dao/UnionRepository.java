package com.dao;

import com.entry.UnionEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UnionRepository extends PagingAndSortingRepository<UnionEntry, Long> {

}