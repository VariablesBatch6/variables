package com.var.var.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.var.var.model.PrimaryAccount;


public interface PrimaryAccountDao extends JpaRepository<PrimaryAccount,Long> {

    PrimaryAccount findByAccountNumber (int accountNumber);
    
    @Query(value = "SELECT max(account_number) FROM primary_Account", nativeQuery = true)
    public String findAccountNumber();
}
