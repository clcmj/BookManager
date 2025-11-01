package com.bookmanager.service;

import com.bookmanager.model.User;
import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    Optional<User> findById(Long userId);
    User updateUserInfo(Long userId, User userDetails);
    User updatePassword(Long userId, String oldPassword, String newPassword);
    void deleteUser(Long userId);
    User updateUserStatus(Long userId, Integer status);
    boolean checkPassword(Long userId, String password);
}