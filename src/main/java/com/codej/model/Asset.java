package com.codej.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Asset {
    @Id
    private Long id;

    private double quantity;
    private double buyPrice;

    @ManyToOne
    private Coin coin;

    @ManyToOne
    private UserEntity user;
}
