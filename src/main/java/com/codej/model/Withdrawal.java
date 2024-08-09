package com.codej.model;

import com.codej.domain.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private WithdrawalStatus status;
    private Long amount;

    @ManyToOne
    private UserEntity user;

    private LocalDateTime date= LocalDateTime.now();
}
