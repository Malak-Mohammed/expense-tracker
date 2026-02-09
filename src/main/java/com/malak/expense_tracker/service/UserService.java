package com.malak.expense_tracker.service;

import com.malak.expense_tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    User updateUser(Long userId, User updatedUser);
    void deleteUser(Long userId);
    List<User> getAllUsers();


}
