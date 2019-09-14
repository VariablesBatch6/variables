package com.var.var.service.serviceImpl;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.var.var.dao.PrimaryAccountDao;
import com.var.var.dao.SavingsAccountDao;
import com.var.var.model.PrimaryAccount;
import com.var.var.model.SavingsAccount;
import com.var.var.service.AccountService;



@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(savingsAccountGen());

		primaryAccountDao.save(primaryAccount);

		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(primaryAccountGen());

		savingsAccountDao.save(savingsAccount);

		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}

	private int primaryAccountGen() {
		if(primaryAccountDao.findAccountNumber() == null)
			return 111111112;
		return Integer.parseInt(primaryAccountDao.findAccountNumber())+1;
	}

	private int savingsAccountGen() {
		if(savingsAccountDao.findAccountNumber() == null)
			return 111111112;
		return Integer.parseInt(savingsAccountDao.findAccountNumber())+1;
	}
	
	
	public PrimaryAccount findToPrimaryAccount(int accountNumber) {
		return primaryAccountDao.findByAccountNumber(accountNumber);
	}
	
	public SavingsAccount findToSavingsAccount(int accountNumber) {
		return savingsAccountDao.findByAccountNumber(accountNumber);
	}

}
