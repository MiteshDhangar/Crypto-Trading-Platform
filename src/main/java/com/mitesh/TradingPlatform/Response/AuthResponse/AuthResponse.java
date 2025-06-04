package com.mitesh.TradingPlatform.Response.AuthResponse;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private boolean status;
    private String message;
    private boolean isTwoFactAuthEnable;
    private String session;
}