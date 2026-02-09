package com.malak.expense_tracker.service;

import com.malak.expense_tracker.dto.UserDTO;
import com.malak.expense_tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO registerUser(User user);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    UserDTO updateUser(Long userId, User updatedUser);

    void deleteUser(Long userId);

    List<UserDTO> getAllUsers();
}