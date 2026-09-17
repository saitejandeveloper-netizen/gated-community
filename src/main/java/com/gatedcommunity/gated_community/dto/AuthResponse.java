package com.gatedcommunity.gated_community.dto;

public record AuthResponse(
        String token,
        String email,
        String role
) {
}
