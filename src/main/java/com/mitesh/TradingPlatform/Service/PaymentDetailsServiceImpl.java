package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Model.PaymentDetails;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Repository.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;


    @Override
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolder,
                                            String ifsc,
                                            String bankName,
                                            User user) {
        PaymentDetails paymentDetails=new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolder);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUsersPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
