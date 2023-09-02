package com.SJcoder.controller;

import java.util.HashSet;
import java.util.Set;
import com.SJcoder.model.Role;
import com.SJcoder.model.applicationUsers;
import com.SJcoder.model.loginDTO;
import com.SJcoder.model.registrationDTO;
import com.SJcoder.repository.roleRepository;
import com.SJcoder.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class adminController {
	
	@Autowired
	private userService userService;
	
	@Autowired
	private roleRepository roleRepository;
	
	@Autowired
	roleRepository roleService;
	
	@GetMapping("/")
	public String adminHome(Model model) {
		
//		For the list of all users
		model.addAttribute("userList",userService.getAllUsers());
		
//		For searching
		loginDTO searchKey = new loginDTO();
		model.addAttribute("searchKey", searchKey);
		return "adminHome.html";
	}
	
//	For Registration
	@GetMapping("/register/")
	public String registerUser(Model model,HttpSession session) {
		
		registrationDTO newUser = new registrationDTO();
		model.addAttribute("newUser", newUser);

		return "create_user.html";
	}
	
	@PostMapping("/register/new/")
	public String saveRegister(@ModelAttribute("newUser") registrationDTO r,HttpSession session) {
		
		// Check if the username/emailid already exists.
	    boolean validUserName = userService.isValidUserName(r.getUserName());
	    boolean validEmailId = userService.isValidEmailId(r.getEmailId());
	    
	    if (!validUserName) {
	        session.setAttribute("validUserName", "User Name alreday exists");
	        session.removeAttribute("validEmailId");
	        session.setAttribute("newUser", r);
	        session.removeAttribute("msg");
	        return "redirect:/admin/register/";
	    }
	    if (!validEmailId) {
	        session.setAttribute("validEmailId", "Email Id alreday exists");
	        session.removeAttribute("validUserName");
	        session.setAttribute("newUser", r);
	        session.removeAttribute("msg");
	        return "redirect:/admin/register/";
	    }
	    session.removeAttribute("validUserName");
	    session.removeAttribute("validEmailId");

	    userService.registerUser(r.getUserName(), r.getEmailId(), r.getPassword());

	    System.out.println("Registration Completed");

	    return "redirect:/admin/";
	}
	
	
//	Editing and deleting an existing data
	@GetMapping("/edit/{id}")
	public String editUserForm(@PathVariable Integer id, Model model,HttpSession session) {		
		
//		To hold the data of user data needs to be updated
		applicationUsers editUser = userService.findUserById(id);	
		model.addAttribute("editUser", editUser);
		return "edit_user.html";
	}
	
	@PostMapping("/{id}")
	public String updateUser(@PathVariable Integer id, @ModelAttribute registrationDTO r,HttpSession session) {
		
		// Check if the username/emailid already exists.
	    boolean validUserName = userService.isValidUserName(r.getUserName());
	    boolean validEmailId = userService.isValidEmailId(r.getEmailId());
	    
	    if (!validUserName) {
	        session.setAttribute("validUserName", "User Name alreday exists");
	        session.removeAttribute("validEmailId");
	        session.setAttribute("newUser", r);
	        session.removeAttribute("msg");
	        return "redirect:/admin/register/";
	    }
	    if (!validEmailId) {
	        session.setAttribute("validEmailId", "Email Id alreday exists");
	        session.removeAttribute("validUserName");
	        session.setAttribute("newUser", r);
	        session.removeAttribute("msg");
	        return "redirect:/admin/register/";
	    }
	    session.removeAttribute("validUserName");
	    session.removeAttribute("validEmailId");

		
		userService.updateUser(id,r.getUserName(),r.getEmailId(),r.getPassword());
		System.out.println("User data updated");
		return "redirect:/admin/";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable Integer id, @ModelAttribute registrationDTO r) {
		
		userService.deleteUser(id);
		System.out.println("User data deleted");
		return "redirect:/admin/";
	}
	
	@PostMapping("/search")
	public String searchUser(@ModelAttribute loginDTO l,Model model) {
		
		System.out.println("Searching");
		applicationUsers searchUser = userService.findUserByName(l.getUserName());
		if(searchUser == null) {
			return "no_result.html";
		}
		else {
			System.out.println("User found");
			model.addAttribute("searchUser", searchUser);
			return "search_result.html";
		}
	}
	
	
//	Role Changing
	@GetMapping("/makeUser/{id}")
	public String makeUser(@PathVariable Integer id) {
		
		applicationUsers toUser = userService.findUserById(id);
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByAuthority("USER").get();
		roles.add(userRole);
		toUser.setAuthorities(roles);
		userService.deleteUser(id);
		userService.changeRole(toUser);
		return "redirect:/admin/";
	}
	
	@GetMapping("/makeAdmin/{id}")
	public String makeAdmin(@PathVariable Integer id) {

		applicationUsers toAdmin = userService.findUserById(id);
		Set<Role> roles = new HashSet<>();
		Role adminRole = roleRepository.findByAuthority("ADMIN").get();
		roles.add(adminRole);
		toAdmin.setAuthorities(roles);
		userService.deleteUser(id);
		userService.changeRole(toAdmin);
		return "redirect:/admin/";
	}
}

