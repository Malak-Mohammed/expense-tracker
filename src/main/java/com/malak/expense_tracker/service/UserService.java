package com.malak.expense_tracker.service;

import com.malak.expense_tracker.dto.RegisterRequest;
import com.malak.expense_tracker.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO registerUser(RegisterRequest request);

    UserDTO assignRole(Long userId, String roleName, String currentUsername);


    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);


    UserDTO updateUser(Long userId, UserDTO updatedUser, String currentUsername);

    void changePassword(Long userId, String oldPassword, String newPassword, String currentUsername);



    void deleteUser(Long userId, String currentUsername);

    List<UserDTO> getAllUsers(String currentUsername);
}