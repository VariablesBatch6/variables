package com.var.var.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.var.var.model.SavingsTransaction;



public interface SavingsTransactionDao extends JpaRepository<SavingsTransaction, Long> {

    List<SavingsTransaction> findAll();
}

