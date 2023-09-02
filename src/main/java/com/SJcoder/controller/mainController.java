package com.SJcoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.SJcoder.model.loginDTO;
import com.SJcoder.model.registrationDTO;
import com.SJcoder.service.*;
import jakarta.servlet.http.HttpSession;
@Controller
public class mainController {
	
	@Autowired
	private userService userService;
	
//	For Login
	@GetMapping({"/", "/login"})
	public String loginPage(Model model) {
//		To hold the data
		loginDTO userLoginAccount = new loginDTO();
		model.addAttribute("userLoginAccount", userLoginAccount);
		return "login.html";
	}
	
//	For Registration
	@GetMapping("/register")
	public String registerPage(Model model,HttpSession session) {

//		To hold the data
		registrationDTO newUser = new registrationDTO();
		model.addAttribute("newUser", newUser);

		return "register.html";
	}
	
	@PostMapping("/register/new")
	public String saveRegister(@ModelAttribute("newUser") registrationDTO r, HttpSession session) {
		

	    System.out.println("Register Sucessfully");
		
		// Check if the username/emailid already exists.
	    boolean validUserName = userService.isValidUserName(r.getUserName());
	    boolean validEmailId = userService.isValidEmailId(r.getEmailId());
	    
	    if (!validUserName) {
	        session.setAttribute("validUserName", "User Name alreday exists");
	        session.removeAttribute("validEmailId");
	        session.setAttribute("newUser", r);
	        return "register.html";
	    }
	    if (!validEmailId) {
	        session.setAttribute("validEmailId", "Email Id alreday exists");
	        session.removeAttribute("validUserName");
	        session.setAttribute("newUser", r);
	        return "register.html";
	    }
	    session.removeAttribute("validUserName");
	    session.removeAttribute("validEmailId");

	    userService.registerUser(r.getUserName(), r.getEmailId(), r.getPassword());


	    return "redirect:/login";
	}
	
//	For handling access denied exception
	@GetMapping("/access-denied")
	public String accessDenied() {

		return "access-denied.html";
	}
}
