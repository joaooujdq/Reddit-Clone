package com.example.RedditClone.controller;

import com.example.RedditClone.dto.AuthenticationResponse;
import com.example.RedditClone.dto.LoginRequest;
import com.example.RedditClone.dto.RegisterRequest;
import com.example.RedditClone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity(OK);

    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);

    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity(OK);
    }


}
