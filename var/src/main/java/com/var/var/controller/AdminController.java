package com.var.var.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.var.var.dao.PrimaryTransactionDao;
import com.var.var.dao.SavingsTransactionDao;
import com.var.var.dao.UserDao;
import com.var.var.model.PrimaryTransaction;
import com.var.var.model.SavingsTransaction;
import com.var.var.model.User;
import com.var.var.service.TransactionService;
import com.var.var.service.UserService;
import com.var.var.service.serviceImpl.EmailService;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private SavingsTransactionDao savingsTransactionDao;

	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;

	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "/user/all")
	public String getAllUsers(Model model) {
		model.addAttribute("user", userService.findUserList());
		return "dashboard";
	}

	@RequestMapping(value = "/user/primary/transaction/{username}", method = RequestMethod.GET)
	public String getPrimaryTransactionList(@PathVariable("username") String username, Model model) {
		System.out.println("Fetched user transaction details");
		model.addAttribute("username", username);
		model.addAttribute("primaryTransactionList", transactionService.findPrimaryTransactionList(username));
		return "primaryTransaction";
	}

	@RequestMapping(value = "/user/savings/transaction/{username}", method = RequestMethod.GET)
	public String getSavingsTransactionList(@PathVariable("username") String username, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("savingsTransactionList", transactionService.findSavingsTransactionList(username));
		return "savingsTransaction";
	}

	@RequestMapping("/user/{username}/enable")
	public String enableUser(@PathVariable("username") String username) {
		userService.enableUser(username);
		emailService.sendMessage(userService.findByUsername(username).getEmail(), "Account Activation",
				"Your Account has been activated Successfully");
		return "redirect:/user/all";
	}

	@RequestMapping("/user/{username}/disable")
	public String diableUser(@PathVariable("username") String username) {
		userService.disableUser(username);
		emailService.sendMessage(userService.findByUsername(username).getEmail(), "Account De-Activation",
				"Your Account has been de-activated");
		return "redirect:/user/all";
	}

	@RequestMapping("/user/{username}/modify")
	public String modifyUserDetails(@PathVariable("username") String username, Model model) {
		model.addAttribute("user", userService.findByUsername(username));
		return "modifyUserDetails";
	}

	@RequestMapping(value = "/user/modify", method = RequestMethod.POST)
	public String modifyUserDetailsPost(@ModelAttribute("user") User user, Model model) {
		userDao.updateFirstName(user.getFirstName(), user.getUsername());
		userDao.updateLastName(user.getLastName(), user.getUsername());
		userDao.updatePhone(user.getPhone(), user.getUsername());

		return "redirect:/user/all";
	}

//	    @RequestMapping(value="/users/savingsTransactionLimitExceded")
//	    public String getUsersWhoseTransferLimitExceded(Model model){
//	    	List<SavingsTransaction> usersWhoseTransferLimitExceded = new ArrayList<SavingsTransaction>();
//	    	for(SavingsTransaction std :savingsTransactionDao.findAll()) {
//	    		if(std.getLimitExceded().equalsIgnoreCase("yes")) {
//	    			usersWhoseTransferLimitExceded.add(std);
//	    		}
//	    	}
//	    	model.addAttribute("usersWhoseTransferLimitExceded", usersWhoseTransferLimitExceded);
//	    	return "primaryLimitTransferExcededUsers";
//	     }
//	    
//	    @RequestMapping(value="/users/primaryTransactionLimitExceded")
//	    public String getUsersWhosePrimaryTransferLimitExceded(Model model){
//	    	List<PrimaryTransaction> usersWhoseTransferLimitExceded = new ArrayList<PrimaryTransaction>();
//	    	for(PrimaryTransaction std : primaryTransactionDao.findAll()) {
//	    		if(std.getLimitExceded().equalsIgnoreCase("yes")) {
//	    			usersWhoseTransferLimitExceded.add(std);
//	    		}
//	    	}
//	    	model.addAttribute("usersWhoseTransferLimitExceded", usersWhoseTransferLimitExceded);
//	    	return "savingsLimitTransferExcededUsers";
//	     }

	@RequestMapping(value = "/users/savingsTransactionLimitExceded")
	@ResponseBody
	public List<SavingsTransaction> getUsersWhoseTransferLimitExceded() {
		List<SavingsTransaction> usersWhoseTransferLimitExceded = new ArrayList<SavingsTransaction>();
		for (SavingsTransaction std : savingsTransactionDao.findAll()) {
			if (std.getLimitExceded().equalsIgnoreCase("yes")) {
				usersWhoseTransferLimitExceded.add(std);
			}
		}
		return usersWhoseTransferLimitExceded;
	}

	@RequestMapping(value = "/users/primaryTransactionLimitExceded")
	@ResponseBody
	public List<PrimaryTransaction> getUsersWhosePrimaryTransferLimitExceded() {
		List<PrimaryTransaction> usersWhoseTransferLimitExceded = new ArrayList<PrimaryTransaction>();
	//	System.out.println(primaryTransactionDao.findAll().get().getLimitExceded());
		for (PrimaryTransaction std : primaryTransactionDao.findAll()) {
			if (std.getLimitExceded().equalsIgnoreCase("yes")) {
				usersWhoseTransferLimitExceded.add(std);
			}
		}
		return usersWhoseTransferLimitExceded;
	}
}
