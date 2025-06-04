package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Domain.VerificationType;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Model.VerificationCode;

public interface VerificationCodeService {
     VerificationCode sendVerificationCode(User user, VerificationType verificationType);
     VerificationCode getVerificationCodeById(Long id) throws Exception;
     VerificationCode getVerificationByUser(Long userId);

     void deleteVerificationCodeById(VerificationCode verificationCode);
}
