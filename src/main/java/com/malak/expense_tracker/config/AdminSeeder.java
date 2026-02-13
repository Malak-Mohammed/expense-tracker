package com.malak.expense_tracker.config;

import com.malak.expense_tracker.model.Role;
import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.RoleRepository;
import com.malak.expense_tracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Profile("local")
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    public AdminSeeder(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AdminProperties adminProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(String... args) {
        String adminUsername = adminProperties.getUsername();
        String adminEmail = adminProperties.getEmail();
        String adminPassword = adminProperties.getPassword();

        userRepository.findByUsername(adminUsername).orElseGet(() -> {
            Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(Collections.singleton(adminRole));

            return userRepository.save(admin);
        });
    }
}