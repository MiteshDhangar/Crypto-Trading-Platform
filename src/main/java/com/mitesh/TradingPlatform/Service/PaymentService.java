package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.Domain.PaymentMethod;
import com.mitesh.TradingPlatform.Model.PaymentOrder;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Response.AuthResponse.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) throws RazorpayException;

    PaymentResponse createStripePaymentLink(User user, Long amount,Long orderId) throws StripeException;

}

