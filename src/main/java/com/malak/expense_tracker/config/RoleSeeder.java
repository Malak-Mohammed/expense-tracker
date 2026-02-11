package com.malak.expense_tracker.config;

import com.malak.expense_tracker.model.Role;
import com.malak.expense_tracker.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        seedRole("ROLE_USER");
        seedRole("ROLE_ADMIN");
    }

    private void seedRole(String roleName) {
        roleRepository.findByRoleName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}