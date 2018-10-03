package com.service;

import com.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService extends BaseService {


    public User login(String username, String password) {


        if ("aaa".equals(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUid("2");

            //TODO 这里做多点登录判断

            return user;
        }

        return null;
    }
}
