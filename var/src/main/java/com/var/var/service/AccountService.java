package com.var.var.service;

import com.var.var.model.PrimaryAccount;
import com.var.var.model.SavingsAccount;




public interface AccountService {

	PrimaryAccount createPrimaryAccount();
	SavingsAccount createSavingsAccount();
	
}
