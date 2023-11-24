package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}