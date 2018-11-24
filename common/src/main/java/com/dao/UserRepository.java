package com.dao;

import com.entry.UserEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends MongoRepository<UserEntry, String> {

    List<UserEntry> findUserEntriesByUserName(String userName);

    Optional<UserEntry> findByUserNameAndPassWord(String userName, String passWord);

    Optional<UserEntry> findByUid(long uid);
}