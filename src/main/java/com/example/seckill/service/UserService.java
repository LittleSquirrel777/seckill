package com.example.seckill.service;

import com.example.seckill.entity.User;

public interface UserService {

    void register(User user);

    User login(String phone, String password);

    User findUserById(int id);

}
