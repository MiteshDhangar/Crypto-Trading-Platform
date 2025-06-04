package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Model.TwoFactorOTP;
import com.mitesh.TradingPlatform.Model.User;

public interface TwoFactorOTPService {

    TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt);
    TwoFactorOTP findByUser(Long userId);
    TwoFactorOTP findById(String id);
    boolean verifyTwofactorOTP(TwoFactorOTP twoFactorOTP,String otp);

    void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP);
}
