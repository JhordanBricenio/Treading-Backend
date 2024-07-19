package com.codej.model;

import com.codej.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ForgotPasswordToken {


       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
       @OneToOne
    private UserEntity user;
    private String otp;
    private VerificationType verificationType;
    private String sendTo;
}
