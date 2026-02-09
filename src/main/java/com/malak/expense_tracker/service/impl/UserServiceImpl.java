package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.dto.UserDTO;
import com.malak.expense_tracker.exception.UserNotFoundException;
import com.malak.expense_tracker.exception.DuplicateEmailException;
import com.malak.expense_tracker.exception.DuplicateUsernameException;
import com.malak.expense_tracker.mapper.UserMapper;
import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.UserRepository;
import com.malak.expense_tracker.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already in use");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already in use");
        }
        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserMapper::toDto);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDto);
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
    public UserDTO updateUser(Long userId, User updatedUser) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        userRepository.findByEmail(updatedUser.getEmail())
                .filter(user -> !user.getUserId().equals(userId))
                .ifPresent(user -> {
                    throw new DuplicateEmailException("Email already in use by another user");
                });

        userRepository.findByUsername(updatedUser.getUsername())
                .filter(user -> !user.getUserId().equals(userId))
                .ifPresent(user -> {
                    throw new DuplicateUsernameException("Username already in use by another user");
                });

        existing.setEmail(updatedUser.getEmail());
        existing.setUsername(updatedUser.getUsername());

        User savedUser = userRepository.save(existing);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}