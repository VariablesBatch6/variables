package com.var.var.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.var.var.model.PrimaryTransaction;


public interface PrimaryTransactionDao extends JpaRepository<PrimaryTransaction, Long> {

    List<PrimaryTransaction> findAll();
    PrimaryTransaction findByPrimaryAccountId(Long primaryAccountId);
}
