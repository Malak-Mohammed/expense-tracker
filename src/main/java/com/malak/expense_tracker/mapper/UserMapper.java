package com.malak.expense_tracker.mapper;

import com.malak.expense_tracker.dto.UserDTO;
import com.malak.expense_tracker.model.Role;
import com.malak.expense_tracker.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toSet())
        );
    }


    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setUserId(dto.userId());
        user.setUsername(dto.username());
        user.setEmail(dto.email());

        return user;
    }
}