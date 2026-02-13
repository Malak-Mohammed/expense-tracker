package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.dto.RegisterRequest;
import com.malak.expense_tracker.dto.UserDTO;
import com.malak.expense_tracker.exception.UserNotFoundException;
import com.malak.expense_tracker.exception.DuplicateEmailException;
import com.malak.expense_tracker.exception.DuplicateUsernameException;
import com.malak.expense_tracker.mapper.UserMapper;
import com.malak.expense_tracker.model.Role;
import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.RoleRepository;
import com.malak.expense_tracker.repository.UserRepository;
import com.malak.expense_tracker.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("Email already in use");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUsernameException("Username already in use");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));
        user.setRoles(Collections.singleton(defaultRole));

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }
    @Override
    public UserDTO assignRole(Long userId, String roleName, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUsername));

        if (!currentUser.hasRole("ADMIN")) {
            throw new AccessDeniedException("Only admins can assign roles");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Role role = roleRepository.findByRoleName("ROLE_" + roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.setRoles(Collections.singleton(role));
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
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO updatedUser, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUsername));

        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));


        if (!existing.equals(currentUser) && !currentUser.hasRole("ADMIN")) {
            throw new AccessDeniedException("You cannot update this user");
        }


        if (!existing.getUsername().equals(updatedUser.username())) {
            throw new IllegalArgumentException("Username cannot be changed once registered");
        }


        userRepository.findByEmail(updatedUser.email())
                .filter(user -> !user.getUserId().equals(userId))
                .ifPresent(user -> { throw new DuplicateEmailException("Email already in use by another user"); });

        existing.setEmail(updatedUser.email());

        User savedUser = userRepository.save(existing);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUsername));

        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (!existing.equals(currentUser) && !currentUser.hasRole("ADMIN")) {
            throw new AccessDeniedException("You cannot change this user's password");
        }

        if (existing.equals(currentUser)) {
            if (!passwordEncoder.matches(oldPassword, existing.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
        }


        existing.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long userId, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUsername));

        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Only owner or ADMIN can delete
        if (!existing.equals(currentUser) && !currentUser.hasRole("ADMIN")) {
            throw new AccessDeniedException("You cannot delete this user");
        }

        userRepository.delete(existing);
    }

    @Override
    public List<UserDTO> getAllUsers(String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUsername));

        if (!currentUser.hasRole("ADMIN")) {
            throw new AccessDeniedException("You cannot view all users");
        }

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}