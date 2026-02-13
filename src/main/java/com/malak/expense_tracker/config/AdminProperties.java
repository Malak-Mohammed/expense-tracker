package com.malak.expense_tracker.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "admin")
@Validated   
public class AdminProperties {

    @NotBlank(message = "Admin username must not be blank")
    private String username;

    @Email(message = "Admin email must be a valid email address")
    @NotBlank(message = "Admin email must not be blank")
    private String email;

    @NotBlank(message = "Admin password must not be blank")
    private String password;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}