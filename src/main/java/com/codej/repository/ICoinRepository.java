package com.codej.repository;

import com.codej.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICoinRepository  extends JpaRepository<Coin, String> {
}
