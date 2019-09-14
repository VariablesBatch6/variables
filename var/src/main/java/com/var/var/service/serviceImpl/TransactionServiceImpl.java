package com.var.var.service.serviceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.var.var.dao.PrimaryAccountDao;
import com.var.var.dao.PrimaryTransactionDao;
import com.var.var.dao.SavingsAccountDao;
import com.var.var.dao.SavingsTransactionDao;
import com.var.var.dao.UserDao;
import com.var.var.model.PrimaryAccount;
import com.var.var.model.PrimaryTransaction;
import com.var.var.model.SavingsAccount;
import com.var.var.model.SavingsTransaction;
import com.var.var.model.User;
import com.var.var.service.TransactionService;
import com.var.var.service.UserService;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;

	@Autowired
	private SavingsTransactionDao savingsTransactionDao;

	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();

		return primaryTransactionList;
	}

	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();

		return savingsTransactionList;
	}

	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-YYYY");
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {

			if (primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)).signum() == -1) {
				throw new Exception("Invalid Transfer");
			} else {
				primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
				savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			}

			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);		

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(simpleDateFormat.format(new Date()),
					"Between account transfer from " + transferFrom + " to " + transferTo, "Account", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {

			if (savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)).signum() == -1 || (savingsAccount
					.getAccountBalance().subtract(new BigDecimal(amount)).compareTo(new BigDecimal("5000")) < 0)) {
				throw new Exception("Minimum 5000 balance should be maintained");
			}
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			SavingsTransaction savingsTransaction = new SavingsTransaction(simpleDateFormat.format(new Date()),
					"Between account transfer from " + transferFrom + " to " + transferTo, "Transfer", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);
		} else {
			throw new Exception("Invalid Transfer");
		}
	}
	
	public void toSomeoneElseTransfer(int accountNumber, String fromAccountType, String amount,
			PrimaryAccount fromPrimaryAccount, SavingsAccount fromSavingsAccount, Principal principal) throws Exception {
		PrimaryAccount toPrimaryAccount = primaryAccountDao.findByAccountNumber(accountNumber);
		SavingsAccount toSavingsAccount = savingsAccountDao.findByAccountNumber(accountNumber);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-YYYY");
		String transferLimitExceded = (Double.parseDouble(amount) > 10000) ? "yes" : "no";
			
		if (toPrimaryAccount != null) {
			if (fromAccountType.equalsIgnoreCase("Primary")) {
				if (fromPrimaryAccount.getAccountBalance().subtract(new BigDecimal(amount)).signum() == -1) {
					throw new Exception("Invalid Transfer");
				}
				fromPrimaryAccount
						.setAccountBalance(fromPrimaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
				primaryAccountDao.save(fromPrimaryAccount);


				PrimaryTransaction primaryTransaction = new PrimaryTransaction(simpleDateFormat.format(new Date()),
						"Transfer to  " +userDao.findByPrimaryAccountId(toPrimaryAccount.getId()).getUsername()+"("+accountNumber+")", "Transfer", "Finished",
						Double.parseDouble(amount), fromPrimaryAccount.getAccountBalance(), transferLimitExceded, fromPrimaryAccount);
				
				primaryTransactionDao.save(primaryTransaction);
				toPrimaryAccount.setAccountBalance(toPrimaryAccount.getAccountBalance().add(new BigDecimal(amount)));
				primaryTransactionDao.save(new PrimaryTransaction(simpleDateFormat.format(new Date()),
						"Recieved from  " +principal.getName()+" (" + fromPrimaryAccount.getAccountNumber()+")" , "Recieved", "Finished",
						Double.parseDouble(amount), toPrimaryAccount.getAccountBalance(),transferLimitExceded, toPrimaryAccount));

			} else if (fromAccountType.equalsIgnoreCase("Savings")) {
				if (fromSavingsAccount.getAccountBalance().subtract(new BigDecimal(amount)).signum() == -1
						|| (fromSavingsAccount.getAccountBalance().subtract(new BigDecimal(amount))
								.compareTo(new BigDecimal("5000")) < 0)) {
					throw new Exception("Minimum 5000 balance should be maintained");
				}
				fromSavingsAccount
						.setAccountBalance(fromSavingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
				savingsAccountDao.save(fromSavingsAccount);

				SavingsTransaction savingsTransaction = new SavingsTransaction(simpleDateFormat.format(new Date()),
						"Transfer to " + userDao.findByPrimaryAccountId(toPrimaryAccount.getId()).getUsername()+"("+accountNumber+")", "Transfer", "Finished",
						Double.parseDouble(amount), fromSavingsAccount.getAccountBalance(), transferLimitExceded, fromSavingsAccount);
				savingsTransactionDao.save(savingsTransaction);
				
				toPrimaryAccount.setAccountBalance(toPrimaryAccount.getAccountBalance().add(new BigDecimal(amount)));
				primaryTransactionDao.save(new PrimaryTransaction(simpleDateFormat.format(new Date()),
						"Recieved from  "+principal.getName()+" (" + fromSavingsAccount.getAccountNumber()+")" , "Recieved", "Finished",
						Double.parseDouble(amount), toPrimaryAccount.getAccountBalance(), transferLimitExceded, toPrimaryAccount));
			}
			

		} else if (toSavingsAccount != null) {
			if (fromAccountType.equalsIgnoreCase("Primary")) {
				if (fromPrimaryAccount.getAccountBalance().subtract(new BigDecimal(amount)).signum() == -1) {
					throw new Exception("Invalid Transfer");
				}
				fromPrimaryAccount
						.setAccountBalance(fromPrimaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
				primaryAccountDao.save(fromPrimaryAccount);

				PrimaryTransaction primaryTransaction = new PrimaryTransaction(simpleDateFormat.format(new Date()),
						"Transfer to  " + userDao.findBySavingsAccountId(toSavingsAccount.getId()).getUsername()+ " ("+accountNumber+")", "Transfer", "Finished",
						Double.parseDouble(amount), fromPrimaryAccount.getAccountBalance(),transferLimitExceded, fromPrimaryAccount);

				primaryTransactionDao.save(primaryTransaction);
				toSavingsAccount.setAccountBalance(toSavingsAccount.getAccountBalance().add(new BigDecimal(amount)));
				savingsTransactionDao.save(new SavingsTransaction(simpleDateFormat.format(new Date()),
						"Recieved from  "+principal.getName()+" (" + fromPrimaryAccount.getAccountNumber()+")" ,"Recieved", "Finished",
						Double.parseDouble(amount), toSavingsAccount.getAccountBalance(),transferLimitExceded, toSavingsAccount));

			} else if (fromAccountType.equalsIgnoreCase("Savings")) {
				if (fromSavingsAccount.getAccountBalance().subtract(new BigDecimal(amount)).signum() == -1
						|| (fromSavingsAccount.getAccountBalance().subtract(new BigDecimal(amount))
								.compareTo(new BigDecimal("5000")) < 0)) {
					throw new Exception("Minimum 5000 balance should be maintained");
				}
				fromSavingsAccount
						.setAccountBalance(fromSavingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
				savingsAccountDao.save(fromSavingsAccount);
				
				SavingsTransaction savingsTransaction = new SavingsTransaction(simpleDateFormat.format(new Date()),
						"Transfer to " + userDao.findBySavingsAccountId(toSavingsAccount.getId()).getUsername()+"("+accountNumber+")", "Transfer", "Finished",
						Double.parseDouble(amount), fromSavingsAccount.getAccountBalance(), transferLimitExceded, fromSavingsAccount);

				savingsTransactionDao.save(savingsTransaction);

				toSavingsAccount.setAccountBalance(toSavingsAccount.getAccountBalance().add(new BigDecimal(amount)));
				savingsTransactionDao.save(new SavingsTransaction(simpleDateFormat.format(new Date()),
						"Recieved from  "+principal.getName()+" (" + fromSavingsAccount.getAccountNumber()+")", "Recieved", "Finished",
						Double.parseDouble(amount), toSavingsAccount.getAccountBalance(),transferLimitExceded, toSavingsAccount));
			}

			
		} else {
			throw new Exception("Account not found " + accountNumber);
		}
	}

}
