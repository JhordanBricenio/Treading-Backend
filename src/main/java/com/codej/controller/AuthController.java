package com.codej.controller;

import com.codej.model.UserEntity;
import com.codej.request.AuthLogin;
import com.codej.response.AuthResponse;
import com.codej.service.IUserService;
import com.codej.service.impl.UserDetailsServiceImpl;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private IUserService userService;

    private UserDetailsServiceImpl userDetailsService;



    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody UserEntity user){
        return new ResponseEntity<>(this.userService.save(user), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLogin userRequest) throws MessagingException {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }


}
