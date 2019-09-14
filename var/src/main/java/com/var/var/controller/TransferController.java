package com.var.var.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.var.var.dao.PrimaryAccountDao;
import com.var.var.dao.SavingsAccountDao;
import com.var.var.dao.UserDao;
import com.var.var.model.PrimaryAccount;
import com.var.var.model.SavingsAccount;
import com.var.var.model.User;
import com.var.var.service.AccountService;
import com.var.var.service.TransactionService;
import com.var.var.service.UserService;
import com.var.var.service.serviceImpl.AccountServiceImpl;
import com.var.var.service.serviceImpl.EmailService;


@Controller
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AccountServiceImpl accountServiceimpl;
	
	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/betweenAccounts", method = RequestMethod.GET)
	public String betweenAccounts(Model model) {
		model.addAttribute("transferFrom", "");
		model.addAttribute("transferTo", "");
		model.addAttribute("amount", "");

		return "betweenAccounts";
	}

	@RequestMapping(value = "/betweenAccounts", method = RequestMethod.POST)
	public String betweenAccountsPost(@ModelAttribute("transferFrom") String transferFrom,
			@ModelAttribute("transferTo") String transferTo, @ModelAttribute("amount") String amount,
			Principal principal, Model model) throws Exception {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		try {
		transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingsAccount);
		emailService.sendMessage(user.getEmail(), "Transaction Deatils", "Transferred from "+transferFrom+" to "+transferTo+" Rupees: "+amount);
		}
		catch(Exception e)	{
			emailService.sendMessage(user.getEmail(), "Transfer Failed", e.getMessage());
			model.addAttribute("invalidTransfer",e.getMessage());
			return "invalidTransfer";
		}
		return "redirect:/userFront";
	}

	@RequestMapping(value = "/toSomeoneElse", method = RequestMethod.GET)
	public String toSomeoneElse(Model model, Principal principal) {
		model.addAttribute("accountType", "");
		return "toSomeoneElse";
	}

	@RequestMapping(value = "/toSomeoneElse", method = RequestMethod.POST)
	public String toSomeoneElsePost(@ModelAttribute("accountNumber") String accountNumber,
			@ModelAttribute("fromAccountType") String fromAccountType, @ModelAttribute("amount") String amount,
			Principal principal, Model model) {
		int toAccountNumber = 0;
		try {
			toAccountNumber = Integer.parseInt(accountNumber);
		}
		catch(NumberFormatException e)	{
			model.addAttribute("inalidTransfer","Account not found "+accountNumber);
		}
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount toPrimaryAccount = accountServiceimpl.findToPrimaryAccount(toAccountNumber);
		SavingsAccount toSavingsAccount = accountServiceimpl.findToSavingsAccount(toAccountNumber);
		
		User recipient ; 				
		try {
			transactionService.toSomeoneElseTransfer(toAccountNumber, fromAccountType, amount, user.getPrimaryAccount(),
					user.getSavingsAccount(),principal);
			recipient = toPrimaryAccount != null ? userDao.findByPrimaryAccountId(toPrimaryAccount.getId()) : userDao.findBySavingsAccountId(toSavingsAccount.getId());
			emailService.sendMessage(user.getEmail(), "Transaction Details", "Transfered Successfully "+amount+" to "+recipient.getUsername());
			emailService.sendMessage(recipient.getEmail(), "Transaction Details", "Recieved Successfully "+amount+" from "+user.getUsername());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception raise");
			emailService.sendMessage(user.getEmail(), "Transfer Failure", e.getMessage());
			model.addAttribute("invalidTransfer",e.getMessage());
			return "invalidTransfer";
		}

		return "redirect:/userFront";
	}
}
