package com.mitesh.TradingPlatform.Request;

import com.mitesh.TradingPlatform.Domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
    private String sendTo;
    private VerificationType verificationType;
}
