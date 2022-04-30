package com.computer.parts.shop.JWT;

import lombok.Data;

@Data
public class JWToken {
    private final String token;
    private final String email;
    private final Boolean isExpired;

    JWToken(String token, String email, Boolean isExpired) {
        this.token = token;
        this.email = email;
        this.isExpired = isExpired;
    }
}
