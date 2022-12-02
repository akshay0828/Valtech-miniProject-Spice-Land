package com.valtech.spring.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.valtech.spring.security.entity.User;
import com.valtech.spring.security.model.RegisterUserModel;
import com.valtech.spring.security.repo.CartLineRepo;
import com.valtech.spring.security.repo.UserReopsitory;
import com.valtech.spring.security.service.CartLineService;
import com.valtech.spring.security.service.OrderService;
import com.valtech.spring.security.service.ProductService;
import com.valtech.spring.security.service.ValtechUserDetailsService;

@Controller
public class DeliveryController {
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

	// Navigation towards login page of delivery person.
	@GetMapping("/delivery/login")
	public String deliveryuser() {
		return "delivery/login";
	}

	/*
	 * Delivery person should provide the username and password . If username
	 * and password of the registered delivery person matches it will navigate
	 * to delivery dashboard else it will display the error message.
	 */
	@PostMapping("/delivery/login")
	public String LoginDelivery(@ModelAttribute RegisterUserModel registerUserModel, Model model)
			throws NullPointerException {
		String url;

		String s1 = "DELIVERY";

		try {

			String s2 = service.getrole(registerUserModel.getUsername());
			if (s1.equals(s2)
					&& registerUserModel.getUsername().equals(service.findUser(registerUserModel.getUsername()))) {
				if (registerUserModel.getPass().equals(service.findUserPass(registerUserModel.getUsername()))) {

					System.out.println(
							registerUserModel.getUsername() + service.findUser(registerUserModel.getUsername()));

					System.out.println("SUCCESS");
					int id = service.getId(registerUserModel.getUsername());

					return url = "redirect:/delivery/deliverhome/" + id;

				}
				String message = "Invalid Username and Password";
				System.out.println(message);
				model.addAttribute("mess", message);
				return "delivery/login";

			}

			else {
				String message = "Invalid Username and Password";
				System.out.println(message);
				model.addAttribute("mess", message);
				return "delivery/login";

			}
		} catch (NullPointerException n) {
			String message = "Invalid Username and Password";
			System.out.println(message);
			model.addAttribute("mess", message);
			return "delivery/login";
		}
	}

	/*
	 * If the delivery person is new user Navigate to the registration page .
	 */

	@GetMapping("delivery/register")
	public String deliveryregister() {
		return "delivery/register";
	}
	/*
	 * delivery person should enter the details
	 * (name,username,email,password,confirmpassword,contact,address). username
	 * should not be repititive if so it displays error message. password and
	 * confirmpassword should be same else error message is displayed.
	 */

	@PostMapping("/delivery/register")
	public String deliveryregisterasrole(@ModelAttribute User user, Model model,
			@RequestParam("username") String username) {

		user.setRole("DELIVERY");
		String u;
		u = service.findUser(username);
		if (u == "false") {

			if (user.getPass().equals(user.getCnfmpass())) {
				service.createUser(user);
				return "redirect:/delivery/login";
			} else {
				model.addAttribute("error", "Password and Confirm Password does not match");

				return "delivery/register";
			}
		}

		model.addAttribute("userna", "Username Already Exists");

		return "delivery/register";

	}
	/*
	 * If delivery person forgets password,will have an option to change the
	 * password
	 * 
	 */

	@GetMapping("/delivery/forgotpassword")
	public String deliveryForgotPassword() {
		return "delivery/forgotpassword";
	}
	/*
	 * Delivery person should enter the username If the username exists it wil
	 * navigate to changepassword page else it will display the error message
	 */

	@PostMapping("/delivery/forgotpassword")

	public String deliverypostForgotPassword(@ModelAttribute User user, @RequestParam("username") String username,
			Model model) {

		String u;
		u = service.findUser(username);

		if (u == "false") {
			model.addAttribute("error", "User Does Not Exists");
			return "delivery/forgotpassword";
		} else {

			model.addAttribute("add", username);
			return "redirect:/delivery/changepassword/" + username;
		}
	}

	/*
	 * If the username entered in the forgot password page, it will navigate to
	 * changepassword page.
	 */
	@GetMapping("/delivery/changepassword/{username}")
	public String deliverychangePassword(@PathVariable("username") String username, Model model) {
		model.addAttribute("userna", username);

		return "delivery/changepassword";
	}
	/*
	 * Delivery person can enter the new password If the password and confirm
	 * password doesn't match it will again ask to enter the username and then
	 * can change the password,password will change successfully.
	 */

	@PostMapping("/delivery/changepassword/{username}")
	public String deliveryupdatechangePassword(@PathVariable("username") String username,
			@RequestParam("pass") String password, @RequestParam("cnfmpass") String confirmPassword, Model model) {
		if (password.equals(confirmPassword)) {
			User u;
			u = service.findentierUser(username);
			u.setPass(password);
			service.updateUser(u);
			System.out.println(u.getPass());
			return "/delivery/login";

		} else {
			model.addAttribute("me", "Password And ConfirmPassword Does Not match");
			model.addAttribute("mes", "Re-Enter the Username");
			return "delivery/forgotpassword";

		}

	}

	/*
	 * Once the delivery person login, It will navigate to the deliverhome.
	 */
	@GetMapping("/delivery/deliverhome/{id}")
	public String deliveryhome(@PathVariable("id") int id, ModelMap model) {
		model.addAttribute("user", service.getuser(id));

		return "delivery/deliverhome";
	}

	/*
	 * If delivery person wants to update the profile Navigate to updateprofile
	 * page.
	 */
	@GetMapping("/delivery/updateprofile/{id}")
	public String deliveryUpdate(@PathVariable("id") int id, Model model) {
		model.addAttribute("user", service.getuser(id));
		return "/delivery/updateprofile";
	}

	/*
	 * Delivery person can Update the profile.
	 */
	@PostMapping("/delivery/updateProfile/{id}")
	public String deliveryUpdateInsert(@PathVariable("id") int id, @ModelAttribute User user, Model model) {
		System.out.println("SUCCESS");
		model.addAttribute("user", service.getuser(id));
		service.updateUser(user);

		return "redirect:/delivery/deliverhome/{id}";
	}

	/*
	 * Delivery person can view the orders place by buyer/user.
	 */

	@GetMapping("/delivery/getOrders/{id}")
	public String getOrders(@PathVariable("id") int id, Model model) {
		model.addAttribute("user", service.getByid(id));
		model.addAttribute("Orders", orderService.findAll());

		return "delivery/getOrders";
	}
	/*
	 * Delivery person can accept the order.
	 */

	@PostMapping("/delivery/getOrders/{userid}/{orderid}/{customerid}")
	public String DeleteProduct(Model model, @PathVariable("userid") int userid, @PathVariable("orderid") int id,
			@PathVariable("customerid") int customerid) {

		orderService.deletebyId(id);

		return "redirect:/delivery/acceptorder/" + userid + "/" + customerid;

	}

	/*
	 * Once delivery person accept the order the details of the buyer/user will
	 * displayed .
	 */

	@GetMapping("/delivery/acceptorder/{id}/{userid}")
	public String acceptorders(@PathVariable("id") int id, Model model, @PathVariable("userid") int userid) {
		model.addAttribute("deliver", service.getByid(id));
		model.addAttribute("user", service.getByid(userid));

		return "delivery/acceptorder";
	}

}
