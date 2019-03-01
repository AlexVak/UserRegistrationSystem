package com.alexvak.urs.services;

import com.alexvak.urs.domain.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User createUser(User user);

    User findById(Long id);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

}
