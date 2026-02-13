package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.RegisterRequest;
import com.malak.expense_tracker.dto.UserDTO;
import com.malak.expense_tracker.response.ApiResponse;
import com.malak.expense_tracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
                                                           @RequestBody UserDTO updatedUser,
                                                           Authentication authentication) {
        String currentUsername = authentication.getName();
        UserDTO savedUser = userService.updateUser(userId, updatedUser, currentUsername);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", savedUser, HttpStatus.OK.value()));
    }

    @PutMapping("/{userId}/assign-role")
    public ResponseEntity<ApiResponse<UserDTO>> assignRole(@PathVariable Long userId,
                                                           @RequestBody Map<String, String> request,
                                                           Authentication authentication) {
        String currentUsername = authentication.getName();
        String roleName = request.get("role");

        UserDTO updatedUser = userService.assignRole(userId, roleName, currentUsername);

        return ResponseEntity.ok(new ApiResponse<>(true, "Role assigned successfully", updatedUser, HttpStatus.OK.value()));
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long userId,
                                                            @RequestBody Map<String, String> request,
                                                            Authentication authentication) {
        String currentUsername = authentication.getName();
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        userService.changePassword(userId, oldPassword, newPassword, currentUsername);

        return ResponseEntity.ok(new ApiResponse<>(true, "Password updated successfully", null, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId,
                                                        Authentication authentication) {
        String currentUsername = authentication.getName();
        userService.deleteUser(userId, currentUsername);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null, HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers(Authentication authentication) {
        String currentUsername = authentication.getName();
        List<UserDTO> users = userService.getAllUsers(currentUsername);
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users, HttpStatus.OK.value()));
    }
}