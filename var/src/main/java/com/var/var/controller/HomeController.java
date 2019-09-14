package com.var.var.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.var.var.dao.RoleDao;
import com.var.var.model.PrimaryAccount;
import com.var.var.model.SavingsAccount;
import com.var.var.model.User;
import com.var.var.model.UserRole;
import com.var.var.service.UserService;
import com.var.var.service.serviceImpl.EmailService;
import com.var.var.service.serviceImpl.OtpService;


@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OtpService otpService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private RoleDao roleDao;
	
	
	@RequestMapping("/")
	public String home(Principal principal, RedirectAttributes redir) {
		User user = userService.findByUsername(principal.getName());
		if(user.isEnabled()) {
			return "redirect:/userFront";
		}
		else {
			return "deActivate";
		}
		
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();

		model.addAttribute("user", user);

		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signupPost(@ModelAttribute("user") User user, Model model) {

		if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
			if (userService.checkEmailExists(user.getEmail())) {
				model.addAttribute("emailExists", true);
			}

			if (userService.checkUsernameExists(user.getUsername())) {
				model.addAttribute("usernameExists", true);
			}

			return "signup";
		} 
		else {	
//			System.out.println(user.getUsername());
        	model.addAttribute("user", user);
        	int otp = otpService.generateOTP(user.getUsername());    		
    		emailService.sendMessage(user.getEmail(), "OTP-SpringBoot", ""+otp);
            return "otppage";
		}
	}

	@RequestMapping(value="/userFront")
	public String userFront(Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();

		model.addAttribute("primaryAccount", primaryAccount);
		model.addAttribute("savingsAccount", savingsAccount);

		return "userFront";
	}
	
	@RequestMapping(value ="/validateOtp", method = RequestMethod.POST)
	public String validateOtp(@RequestParam("otpnum") int otpnum, @ModelAttribute("user")User user){		
		String username = user.getUsername();		
		if(otpnum >= 0){
			int serverOtp = otpService.getOtp(username);
			System.out.println(otpnum+" "+serverOtp);
			if(serverOtp > 0){
				if(otpnum == serverOtp){
					Set<UserRole> userRoles = new HashSet<>();
					userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));

					userService.createUser(user, userRoles);
					otpService.clearOTP(username);
					return "redirect:/index";
				}else{
					return "otppage";	
				}
			}else {
				return "signup";			
			}
		}else {
			return "otppage";	
		}
	}
}
