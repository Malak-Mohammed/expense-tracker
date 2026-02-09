package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.UserRepository;
import com.malak.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        userRepository.findByEmail(updatedUser.getEmail())
                .filter(user -> !user.getUserId().equals(userId))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Email already in use by another user");
                });


        userRepository.findByUsername(updatedUser.getUsername())
                .filter(user -> !user.getUserId().equals(userId))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Username already in use by another user");
                });


        existing.setEmail(updatedUser.getEmail());
        existing.setUsername(updatedUser.getUsername());

        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
