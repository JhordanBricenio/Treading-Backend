package com.codej.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse{
        String jwt;
        boolean status;
        String message;
        Boolean     isTwoFactorAuthEnabled;
        String session;
}