package com.mitesh.TradingPlatform.Controller;

import com.mitesh.TradingPlatform.Response.AuthResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/home")
    public String home(){
        return "welcome to trading platform";
    }
}
