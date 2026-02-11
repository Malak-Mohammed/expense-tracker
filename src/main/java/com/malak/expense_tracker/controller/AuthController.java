package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.RegisterRequest;
import com.malak.expense_tracker.dto.UserDTO;
import com.malak.expense_tracker.response.ApiResponse;
import com.malak.expense_tracker.security.JwtUtil;
import com.malak.expense_tracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody RegisterRequest request) {
        UserDTO registeredUser = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User registered successfully", registeredUser, HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody RegisterRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", token, HttpStatus.OK.value()));
    }
}