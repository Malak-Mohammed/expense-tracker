package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.RegisterRequest;
import com.malak.expense_tracker.dto.UserDTO;
import com.malak.expense_tracker.response.ApiResponse;
import com.malak.expense_tracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody RegisterRequest request) {
        UserDTO savedUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User registered successfully", savedUser, HttpStatus.CREATED.value()));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User found", user, HttpStatus.OK.value())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null, HttpStatus.NOT_FOUND.value())));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User found", user, HttpStatus.OK.value())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null, HttpStatus.NOT_FOUND.value())));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long userId,
                                                           @RequestBody UserDTO updatedUser) {
        UserDTO savedUser = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", savedUser, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null, HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users, HttpStatus.OK.value()));
    }
}