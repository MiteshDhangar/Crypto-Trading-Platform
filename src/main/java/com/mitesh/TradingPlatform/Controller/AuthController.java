package com.mitesh.TradingPlatform.Controller;

import com.mitesh.TradingPlatform.Config.JwtProvider;
import com.mitesh.TradingPlatform.Model.TwoFactorOTP;
import com.mitesh.TradingPlatform.Model.User;
import com.mitesh.TradingPlatform.Repository.TwoFactorOTPRepo;
import com.mitesh.TradingPlatform.Repository.UserRepository;
import com.mitesh.TradingPlatform.Response.AuthResponse.AuthResponse;
import com.mitesh.TradingPlatform.Service.CustomUserDetailsService;
import com.mitesh.TradingPlatform.Service.EmailService;
import com.mitesh.TradingPlatform.Service.TwoFactorOTPService;
import com.mitesh.TradingPlatform.Service.WatchListService;
import com.mitesh.TradingPlatform.utils.Otputils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOTPService twoFactorOTPService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private WatchListService watchListService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        User isEmailExist=userRepository.findByEmail(user.getEmail());
        if(isEmailExist!=null){
            throw new Exception("Email is already register/used with another account");
        }
        User newUser=new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());
        newUser.setTwoFactorAuth(user.getTwoFactorAuth());

        User savedUser= userRepository.save(newUser);

        watchListService.createWatchList(savedUser);

        Authentication auth=new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt= JwtProvider.generateToken(auth);

        AuthResponse response=new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Registered Successfully");
        response.setTwoFactAuthEnable(true);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String userName= user.getEmail();
        String password=user.getPassword();

        Authentication auth=authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt= JwtProvider.generateToken(auth);

        User authUser =userRepository.findByEmail(userName);
        if(user.getTwoFactorAuth().isEnable()){


            AuthResponse response=new AuthResponse();
            response.setMessage("Two Factor Auth is Enabled");
            response.setTwoFactAuthEnable(true);
            String otp= Otputils.generateOtp();

            TwoFactorOTP oldTwoFactorOtp=twoFactorOTPService.findByUser(authUser.getId());
            if (oldTwoFactorOtp!=null){
                twoFactorOTPService.deleteTwoFactorOTP(oldTwoFactorOtp);
            }
            TwoFactorOTP newTwoFactorOTP=twoFactorOTPService.createTwoFactorOTP(authUser,otp,jwt);
            emailService.sendVerificationOtpEmail(userName,otp);

            response.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        AuthResponse response=new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Login Success");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(userName);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid Username");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(
            @PathVariable String otp,
            @RequestParam String id) throws Exception {

        TwoFactorOTP twoFactorOTP=twoFactorOTPService.findById(id);
        if(twoFactorOTPService.verifyTwofactorOTP(twoFactorOTP,otp)){
            twoFactorOTPService.deleteTwoFactorOTP(twoFactorOTP);

            AuthResponse response = new AuthResponse();
            response.setJwt(twoFactorOTP.getJwt());
            response.setStatus(true);  // Changed to true for successful auth
            response.setMessage("Two Factor Authentication Verified");
            response.setTwoFactAuthEnable(true);
            response.setSession(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new Exception("Invalid Otp");
    }
}
