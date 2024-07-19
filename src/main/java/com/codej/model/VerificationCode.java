package com.codej.model;

import com.codej.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "verification_type")
public class VerificationCode {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;

    @OneToOne
    private UserEntity user;
    private String email;
    private String mobile;
    private VerificationType verificationType;
}
