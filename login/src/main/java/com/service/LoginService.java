package com.service;

import com.dao.UserRepository;
import com.entry.UserEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService extends BaseService<UserEntry, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    MongoRepository<UserEntry, String> getRepository() {
        return userRepository;
    }

    public UserEntry login(String username, String password) {
        //TODO 多点登录判断
        Optional<UserEntry> user = userRepository.findByUserNameAndPassWord(username, password);

        return user.orElseGet(() -> {
            UserEntry userEntry = new UserEntry();
            userEntry.setUserName(username);
            userEntry.setPassWord(password);
            userEntry.setUid("2");
            userRepository.save(userEntry);
            return userEntry;
        });
    }
}
