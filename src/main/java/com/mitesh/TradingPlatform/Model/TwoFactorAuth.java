package com.mitesh.TradingPlatform.Model;

import com.mitesh.TradingPlatform.Domain.VerificationType;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class TwoFactorAuth {
    private boolean isEnable = true;
    private VerificationType sendTo;
}


