package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Model.PaymentDetails;
import com.mitesh.TradingPlatform.Model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolder,
                                            String ifsc,
                                            String bankName,
                                            User user);

    public PaymentDetails getUsersPaymentDetails(User user);
}
