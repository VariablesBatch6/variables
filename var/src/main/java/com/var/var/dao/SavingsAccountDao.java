package com.var.var.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.var.var.model.SavingsAccount;


public interface SavingsAccountDao extends JpaRepository<SavingsAccount, Long> {

    SavingsAccount findByAccountNumber (int accountNumber);

    @Query(value = "SELECT max(account_number) FROM savings_account", nativeQuery = true)
    public String findAccountNumber();
}
