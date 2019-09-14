package com.var.var.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.var.var.model.PrimaryAccount;
import com.var.var.model.PrimaryTransaction;
import com.var.var.model.SavingsAccount;
import com.var.var.model.SavingsTransaction;
import com.var.var.model.User;
import com.var.var.service.TransactionService;
import com.var.var.service.UserService;



/**
 * @author variables
 *
 */
/**
 * @author Abridge Solutions
 *
 */
/**
 * @author Abridge Solutions
 *
 */
/**
 * @author Abridge Solutions
 *
 */
/**
 * @author Abridge Solutions
 *
 */
/**
 * @author Abridge Solutions
 *
 */
/**
 * @author Abridge Solutions
 *
 */
@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionService transactionService;

	/**
	 * @param model is to add primary transaction list and account details to the primary account page
	 * @param principcal holds the authentication information
	 * @return primaryAccount view where primary account details and statements are displayed
	 */
	@RequestMapping("/primaryAccount")
	public String primaryAccount(Model model, Principal principal) {
		List<PrimaryTransaction> primaryTransactionList = transactionService
				.findPrimaryTransactionList(principal.getName());

		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();

		model.addAttribute("primaryAccount", primaryAccount);
		model.addAttribute("primaryTransactionList", primaryTransactionList);

		return "primaryAccount";
	}
	
	
	/**
	 * @param model is to add savings transaction list and account details to the primary account page
	 * @param principcal holds the authentication information
	 * @return savingsAccount view where savings account details and statements are displayed
	 */
	@RequestMapping("/savingsAccount")
	public String savingsAccount(Model model, Principal principal) {
		List<SavingsTransaction> savingsTransactionList = transactionService
				.findSavingsTransactionList(principal.getName());
		User user = userService.findByUsername(principal.getName());
		SavingsAccount savingsAccount = user.getSavingsAccount();

		model.addAttribute("savingsAccount", savingsAccount);
		model.addAttribute("savingsTransactionList", savingsTransactionList);

		return "savingsAccount";
	}
}
