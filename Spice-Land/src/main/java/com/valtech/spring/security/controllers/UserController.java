package com.valtech.spring.security.controllers;

import java.time.LocalDate;
import java.util.ArrayList;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.valtech.spring.security.entity.CartLine;
import com.valtech.spring.security.entity.Orders;
import com.valtech.spring.security.entity.Products;
import com.valtech.spring.security.entity.User;
import com.valtech.spring.security.model.RegisterUserModel;
import com.valtech.spring.security.repo.CartLineRepo;
import com.valtech.spring.security.repo.OrderRepository;
import com.valtech.spring.security.repo.UserReopsitory;
import com.valtech.spring.security.service.CartLineService;
import com.valtech.spring.security.service.OrderService;
import com.valtech.spring.security.service.ProductService;
import com.valtech.spring.security.service.ValtechUserDetailsService;

@Controller
public class UserController {
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

	@Autowired
	private OrderService orderService;

	// Navigation towards login page of buyer/user.

	@GetMapping("user/login")
	public String indexuser() {
		return "user/login";
	}
	/*
	 * Buyer/User should provide the username and password . If username and
	 * password of the registered buyer matches it will navigate to buyer/user
	 * dashboard else it will display the error message.
	 */

	@PostMapping("/user/login")
	public String LoginUser(@ModelAttribute RegisterUserModel registerUserModel, Model model) throws Exception {
		String url;

		String s1 = "USER";

		try {

			String s2 = service.getrole(registerUserModel.getUsername());
			if (s1.equals(s2)
					&& registerUserModel.getUsername().equals(service.findUser(registerUserModel.getUsername()))) {
				if (registerUserModel.getPass().equals(service.findUserPass(registerUserModel.getUsername()))) {

					System.out.println(
							registerUserModel.getUsername() + service.findUser(registerUserModel.getUsername()));

					System.out.println("SUCCESS");
					int id = service.getId(registerUserModel.getUsername());

					return url = "redirect:/user/userhome/" + id;

				}
				String message = "Invalid Username and Password";
				System.out.println(message);
				model.addAttribute("mess", message);
				return "user/login";

			}

			else {
				String message = "Invalid Username and Password";
				System.out.println(message);
				model.addAttribute("mess", message);
				return "user/login";

			}
		} catch (Exception n) {
			String message = "Invalid Username and Password";
			System.out.println(message);
			model.addAttribute("mess", message);
			return "user/login";
		}
	}
	/*
	 * If the buyer/user is new  Navigate to the registration page .
	 */

	@GetMapping("user/register")
	public String userregister() {
		return "user/register";
	}

	/*
	 * Buyer/User should enter the details
	 * (name,username,email,password,confirmpassword,contact,address). username
	 * should not be repititive if so it displays error message. password and
	 * confirmpassword should be same else error message is displayed.
	 */

	@PostMapping("/user/register")
	public String userregisterasrole(@ModelAttribute User user, Model model) {

		user.setRole("USER");
		String u;
		u = service.findUser(user.getUsername());
		if (u == "false") {
			if (user.getPass().equals(user.getCnfmpass())) {
				service.createUser(user);
				return "redirect:/user/login";
			} else {
				model.addAttribute("error", "Password and Confirm Password does not match");

				return "user/register";
			}
		}
		model.addAttribute("userna", "Username Already Exists");

		return "user/register";

	}
	/*
	 * If buyer/user forgets password,will have an option to change the password
	 * 
	 */

	@GetMapping("/user/forgotpassword")
	public String userForgotPassword() {
		return "user/forgotpassword";
	}
	/*
	 * Buyer/User should enter the username If the username exists it wil
	 * navigate to changepassword page else it will display the error message
	 */

	@PostMapping("/user/forgotpassword")

	public String userpostForgotPassword(@ModelAttribute User user, @RequestParam("username") String username,
			Model model) {

		String u;
		u = service.findUser(username);

		if (u == "false") {
			model.addAttribute("error", "User Does Not Exists");
			return "user/forgotpassword";
		} else {

			model.addAttribute("add", username);
			return "redirect:/user/changepassword/" + username;
		}
	}
	/*
	 * If the username entered in the forgot password page, it will navigate to
	 * changepassword page.
	 */

	@GetMapping("/user/changepassword/{username}")
	public String userchangePassword(@PathVariable("username") String username, Model model) {
		model.addAttribute("userna", username);

		return "user/changepassword";
	}
	/*
	 * Buyer/User can enter the new password If the password and confirm
	 * password doesn't match it will again ask to enter the username and then
	 * can change the password, password will change successfully.
	 */

	@PostMapping("/user/changepassword/{username}")
	public String userupdatechangePassword(@PathVariable("username") String username,
			@RequestParam("pass") String password, @RequestParam("cnfmpass") String confirmPassword, Model model) {
		if (password.equals(confirmPassword)) {
			User u;
			u = service.findentierUser(username);
			u.setPass(password);
			service.updateUser(u);
			System.out.println(u.getPass());
			return "/user/login";

		} else {
			model.addAttribute("me", "Password And ConfirmPassword Does Not match");
			model.addAttribute("mes", "Re-Enter the Username");
			return "user/forgotpassword";

		}

	}
	/*
	 * Once the buyer/user login, It will navigate to the userhome.
	 */

	@GetMapping("user/userhome/{id}")
	public String userhome(@PathVariable("id") int id, ModelMap model) {

		System.out.println(id);

		User u = service.getUsername(id);
		System.out.println(service.getUsername(id));
		model.addAttribute("add", u.getName());
		model.addAttribute("user", u.getId());
		model.addAttribute("Products", productservice.getAllProducts());

		model.addAttribute("users", service.getAlluser());

		return "user/userhome";
	}
	/*
	 * If buyer/user wants to update the profile Navigate to updateprofile page.
	 */

	@GetMapping("/user/updateprofile/{id}")
	public String userUpdate(@PathVariable("id") int id, Model model) {
		model.addAttribute("user", service.getuser(id));
		return "/user/updateprofile";
	}
	/*
	 * Buyer/User can Update the profile.
	 */

	@PostMapping("/user/updateprofile/{id}")
	public String userUpdateInsert(@PathVariable("id") int id, @ModelAttribute User user, Model model) {
		System.out.println("SUCCESS");
		model.addAttribute("user", service.getuser(id));
		service.updateUser(user);

		return "redirect:/user/userhome/{id}";
	}
	/*
	 * Buyer/User can add the required products to the cart.
	 */

	@GetMapping("user/userhome/{id}/{prod_id}")
	public String userhome1(@PathVariable("id") int id, @PathVariable("prod_id") int prod_id, ModelMap model,
			CartLine cartLine) throws Exception {

		try {
			System.out.println(id);

			User u = service.getUsername(id);
			System.out.println(service.getUsername(id));

			model.addAttribute("add", u.getName());
			model.addAttribute("user", u.getId());

			model.addAttribute("Products", productservice.getAllProducts());

			model.addAttribute("users", service.getAlluser());

			Products p = productservice.getProduct(prod_id);

			int check_User_id = cartLineService.findUserIdAndProdId_onlyUserId(id, prod_id);

			int check_Prod_id = cartLineService.findUserIdAndProdId_onlyProdId(id, prod_id);

			CartLine check = cartLineService.findUserIdAndProdId(id, prod_id);

			if (check_User_id == id & check_Prod_id == prod_id) {

				CartLine c = new CartLine(prod_id, p.getProductName(), p.getPrice(), p.getUserid(), id);

				check.setQuantity(check.getQuantity() + 1);

				cartLineService.createCartLine(check);

			}

			model.addAttribute("cartLine", cartLineService.findAll());

		} catch (DataIntegrityViolationException e)

		{
			Products p = productservice.getProduct(prod_id);
			CartLine cart = cartLineService.findByProId(prod_id);

			cartLine.setId(cart.getId());
			cartLine.setProdid(prod_id);
			cartLine.setProductName(p.getProductName());
			cartLine.setPrice(p.getPrice());
			cartLine.setQuantity(cart.getQuantity() + 1);
			System.out.println(cartLine.getQuantity());

			cartLineService.createCartLine(cartLine);

		}

		catch (NullPointerException n) {
			Products p = productservice.getProduct(prod_id);

			CartLine c = new CartLine(prod_id, p.getProductName(), p.getPrice(), p.getUserid(), id);

			cartLine.setAdminIds(p.getUserid());
			cartLine.setUserid(id);

			c.setUserid(id);

			cartLineService.createCartLine(c);
		}

		return "user/userhome";
	}
	/*
	 * Buyer/User can view the cart items.
	 */

	@GetMapping("/user/cart/{id}")
	public String cart(ModelMap model, @PathVariable("id") int user_id) {
		model.addAttribute("user", service.getuser(user_id));
		if (cartLineService.getAllordersByuserid(user_id).size() == 0) {
			model.addAttribute("error", "Please add Items to cart");
		}
		model.addAttribute("cartLine", cartLineService.getAllordersByuserid(user_id));

		return "user/cart";
	}
	/*
	 * Buyer/User can delete the items added to the cart.
	 */

	@PostMapping("/user/cart/{id}/{userid}")

	public String DeleteCartLine(Model model, @PathVariable("id") int id, @PathVariable("userid") int user_id) {

		System.out.println("DELETING");

		cartLineService.deleteCartLine(id);

		return "redirect:/user/cart/" + user_id;

	}
	/*
	 * If Buyer/User wish to place the order, it will navigate to payment page.
	 */

	@GetMapping("/user/payment/{id}")
	public String payment(ModelMap model, @PathVariable("id") int id) {

		if (cartLineService.getAllordersByuserid(id).size() == 0) {
			model.addAttribute("error", "Please add Items to cart");
			return "redirect:/user/cart/" + id;
		}
		model.addAttribute("user", service.getuser(id));
		return "user/payment";
	}
	/*
	 * Buyer/User should enter the payment details .
	 */

	@PostMapping("/user/payment/{id}")

	public String SaveOrders(@PathVariable("id") int id) {

		ArrayList<Integer> cart_ids = cartLineService.findAllId(id);
		ArrayList<Integer> admin_ids = cartLineService.findAllAdminId(id);

		Orders o = new Orders();
		o.setUser_id(id);
		o.setCartIds(cart_ids);
		o.setDate(LocalDate.now());
		o.setAdminIds(admin_ids);

		orderService.saveOrder(o);

		System.out.println("PAYMENT DONE ");

		return "redirect:/user/orderTracking/" + id;
	}
	/*
	 * Once the order placed, buyer/seller can track the order.
	 */

	@GetMapping("/user/orderTracking/{id}")
	public String order(ModelMap model, @PathVariable("id") int id) {

		model.addAttribute("user", service.getuser(id));

		model.addAttribute("cartLine", cartLineService.getAllordersByuserid(id));

		return "user/orderTracking";
	}
	/*
	 * Once the order is placed cart will be empty.
	 */

	@PostMapping("/user/orderTracking/{id}")
	public String feedback(ModelMap model, @PathVariable("id") int id) {

		cartLineService.EmptyCart(id);

		return "redirect:/user/feedback/" + id;
	}
	/*
	 * After order recieved buyer/user should provide feedback.
	 */

	@GetMapping("/user/feedback/{id}")
	public String feedbacksubmit(@PathVariable("id") int id, Model model) {
		model.addAttribute("user", id);
		return "user/feedback";
	}

}
