package com.malak.expense_tracker.dto;

import java.util.Set;

public record UserDTO(Long userId,
                      String username,
                      String email,
                      Set<String> roles) {
}
