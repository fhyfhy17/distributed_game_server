package com.dao.cache;

import com.dao.UserRepository;
import com.entry.UserEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NoRepositoryBean
@CacheConfig(cacheNames = "UserEntryCache")
public class UserEntryCacheRepository {

    @Autowired
    private UserRepository userRepository;


    public Optional<UserEntry> findByUserNameAndPassWord(String userName, String passWord) {
        return userRepository.findByUserNameAndPassWord(userName, passWord);
    }

    public UserEntry findByUid(long uid) {
        return userRepository.findByUid(uid).orElse(null);
    }

    @Cacheable
    public UserEntry findById(String id) {
        return userRepository.findById(id).orElse(null);
    }


    @CacheEvict(key = "#userEntry.id")
    public UserEntry save(UserEntry userEntry) {
        return userRepository.save(userEntry);
    }

    @CacheEvict(key = "#userEntry.id")
    public void delete(UserEntry userEntry) {
        userRepository.delete(userEntry);
    }

    @CacheEvict
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}