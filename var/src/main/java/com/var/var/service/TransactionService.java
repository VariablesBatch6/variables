package com.var.var.service;

import java.security.Principal;
import java.util.List;

import com.var.var.model.PrimaryAccount;
import com.var.var.model.PrimaryTransaction;
import com.var.var.model.SavingsAccount;
import com.var.var.model.SavingsTransaction;



public interface TransactionService {
	List<PrimaryTransaction> findPrimaryTransactionList(String username);

	List<SavingsTransaction> findSavingsTransactionList(String username);

	void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount,
			SavingsAccount savingsAccount) throws Exception;
	void toSomeoneElseTransfer(int accountNumber, String fromAccountType, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount, Principal principal) throws Exception;


}
