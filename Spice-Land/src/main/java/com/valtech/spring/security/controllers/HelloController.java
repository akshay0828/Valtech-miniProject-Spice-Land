package com.valtech.spring.security.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.valtech.spring.security.entity.CartLine;
import com.valtech.spring.security.entity.Products;
import com.valtech.spring.security.entity.User;
import com.valtech.spring.security.model.RegisterUserModel;
import com.valtech.spring.security.repo.CartLineRepo;
import com.valtech.spring.security.repo.UserReopsitory;
import com.valtech.spring.security.service.CartLineService;
import com.valtech.spring.security.service.ProductService;
import com.valtech.spring.security.service.ValtechUserDetailsService;

//@RestController
@Controller
public class HelloController {
	@Autowired
	private UserReopsitory userRepository;

	@Autowired
	private ValtechUserDetailsService service;

	@Autowired
	private ProductService productservice;

	int uid;

	@Autowired
	private CartLineRepo cartRepo;

	@Autowired
	private CartLineService cartLineService;

	@GetMapping("/register")
	public String register() {
		return "/register";
	}

	@PostMapping("/register")
	// @ResponseBody
	public String registerUser(@ModelAttribute User user) {

		// if(User.h){
		user.setRole("ADMIN");
		service.createUser(user);
		// service.createUser(User.withUsername(registerUserModel.getEmail()).password(registerUserModel.getPass()).roles("USER").build());
		return "/login";

		// }else{
		// return "register";
		// }

	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("register/login")
	public String login1() {
		return "login";
	}

	@PostMapping("/login")
	// @ResponseBody
	public String loginUser(@ModelAttribute RegisterUserModel registerUserModel) {

		// String us=registerUserModel.getUsername();
		if (service.LoginValidator(registerUserModel.getUsername()) == 1) {
			return "user/home";
		}

		return "failure";
	}

	@GetMapping("/seller")
	public String admin() {
		return "Hello seller";
	}

	@GetMapping("/index")
	public String index() {
		return "Index";
	}

	@GetMapping("/customer")
	public String customer(Model model) {
		model.addAttribute("Products", productservice.getAllProducts());
		return "customer";
	}

	

	@GetMapping("/user")
	public String user() {
		return "Hello User";
	}

}
