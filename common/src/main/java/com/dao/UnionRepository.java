package com.dao;

import com.entry.UnionEntry;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

@Component
public interface UnionRepository extends PagingAndSortingRepository<UnionEntry, Long> {

}