package com.gatedcommunity.gated_community.dto;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Phone") String phone,
        @Size(min = 8, message = "Password must be at least 8 characters") String password,
        String flatNumber
) {
}
