package com.mitesh.TradingPlatform.Service;
import com.mitesh.TradingPlatform.Response.AuthResponse.ApiResponse;
public interface ChatBotService {
    ApiResponse getCoinDetails(String prompt) throws Exception;

    String simpleChat(String prompt);}
