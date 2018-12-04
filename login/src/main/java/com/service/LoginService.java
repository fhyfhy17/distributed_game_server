package com.service;

import cn.hutool.core.lang.Snowflake;
import com.dao.UserRepository;
import com.entry.UserEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Snowflake snowflake;

    public UserEntry login(String username, String password) {
        //TODO 多点登录判断
        Optional<UserEntry> user = userRepository.findByUserNameAndPassWord(username, password);
//        user.ifPresent(userEntry -> userRepository.save(userEntry));
        return user.orElseGet(() -> {
            UserEntry userEntry = new UserEntry(snowflake.nextId());
            userEntry.setUserName(username);
            userEntry.setPassWord(password);
            userRepository.save(userEntry);
            return userEntry;
        });
    }
}
