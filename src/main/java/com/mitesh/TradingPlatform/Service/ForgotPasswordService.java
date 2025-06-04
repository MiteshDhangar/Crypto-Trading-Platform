package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Domain.VerificationType;
import com.mitesh.TradingPlatform.Model.ForgotPasswordToken;
import com.mitesh.TradingPlatform.Model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType,String sendTo);
    ForgotPasswordToken findById(String id);
    ForgotPasswordToken findByUser(Long userId);
    void deleteToken(ForgotPasswordToken token);
}
