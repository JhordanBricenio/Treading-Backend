package com.codej.request;

import com.codej.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String sendTo;
    private VerificationType verificationType;
}
