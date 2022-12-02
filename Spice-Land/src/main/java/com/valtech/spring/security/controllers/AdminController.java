
package com.valtech.spring.security.controllers;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.valtech.spring.security.entity.Products;
import com.valtech.spring.security.entity.User;
import com.valtech.spring.security.repo.CartLineRepo;
import com.valtech.spring.security.repo.UserReopsitory;
import com.valtech.spring.security.service.CartLineService;
import com.valtech.spring.security.service.ProductService;
import com.valtech.spring.security.service.ValtechUserDetailsService;

@Controller
public class AdminController {

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

	// Navigation towards login page of admin/seller.
	@GetMapping("admin/login")
	public String indexadmin() {
		return "admin/login";
	}

	/*
	 * Seller/Admin should provide the username and password . If username and
	 * password of the registered seller matches it will navigate to
	 * seller/admin dashboard else it will display the error message.
	 */
	@PostMapping("/admin/login")
	public String Loginadmin(@ModelAttribute User registerUserModel, ModelMap model) throws NullPointerException {
		String url;

		String s1 = "ADMIN";

		try {

			String s2 = service.getrole(registerUserModel.getUsername());
			if (s1.equals(s2)
					&& registerUserModel.getUsername().equals(service.findUser(registerUserModel.getUsername()))) {
				if (registerUserModel.getPass().equals(service.findUserPass(registerUserModel.getUsername()))) {

					System.out.println(
							registerUserModel.getUsername() + service.findUser(registerUserModel.getUsername()));
					System.out.println("PASSWORD");
					System.out.println(registerUserModel.getPass() + service.findUser(registerUserModel.getPass()));

					System.out.println("SUCCESS");
					int id = service.getId(registerUserModel.getUsername());

					uid = id;

					return url = "redirect:/admin/adminhome/" + id;

				}

				String message = "Invalid Username and Password";
				System.out.println(message);
				model.addAttribute("mess", message);
				return "admin/login";

			}

			else {
				String message = "Invalid Username and Password";
				System.out.println(message);
				model.addAttribute("mess", message);
				return "admin/login";

			}

		} catch (NullPointerException n) {
			String message = "Invalid Username and Password";
			System.out.println(message);
			model.addAttribute("mess", message);
			return "admin/login";
		}
	}

	/*
	 * If the seller/admin is new user Navigate to the registration page .
	 */
	@GetMapping("admin/register")
	public String adminregister() {
		return "admin/register";
	}

	/*
	 * Seller/Admin should enter the details
	 * (name,username,email,password,confirmpassword,contact,address). username
	 * should not be repititive if so it displays error message. password and
	 * confirmpassword should be same else error message is displayed.
	 */
	@PostMapping("/admin/register")
	public String adminregisterasrole(@ModelAttribute User user, Model model,
			@RequestParam("username") String username) {

		user.setRole("ADMIN");
		String u;
		u = service.findUser(username);
		if (u == "false") {

			if (user.getPass().equals(user.getCnfmpass())) {
				service.createUser(user);
				return "redirect:/admin/login";
			} else {
				model.addAttribute("error", "Password and Confirm Password does not match");

				return "admin/register";
			}
		}

		model.addAttribute("userna", "Username Already Exists");

		return "admin/register";

	}
	/*
	 * If seller/admin forgets password,will have an option to change the
	 * password
	 * 
	 */

	@GetMapping("/admin/forgotpassword")
	public String adminForgotPassword() {
		return "admin/forgotpassword";
	}

	/*
	 * Seller/Admin should enter the username If the username exists it wil
	 * navigate to changepassword page else it will display the error message
	 */

	@PostMapping("/admin/forgotpassword")

	public String adminpostForgotPassword(@ModelAttribute User user, @RequestParam("username") String username,
			Model model) {

		String u;
		u = service.findUser(username);

		if (u == "false") {
			model.addAttribute("error", "User Does Not Exists");
			return "admin/forgotpassword";
		} else {

			model.addAttribute("add", username);
			return "redirect:/admin/changepassword/" + username;
		}
	}

	/*
	 * If the username entered in the forgot password page, it will navigate to
	 * changepassword page.
	 */

	@GetMapping("/admin/changepassword/{username}")
	public String adminchangePassword(@PathVariable("username") String username, Model model) {
		model.addAttribute("userna", username);

		return "admin/changepassword";
	}
	/*
	 * Seller/admin can enter the new password If the password and confirm
	 * password doesn't match it will again ask to enter the username and then
	 * can change the password,password will change successfully.
	 */

	@PostMapping("/admin/changepassword/{username}")
	public String adminupdatechangePassword(@PathVariable("username") String username,
			@RequestParam("pass") String password, @RequestParam("cnfmpass") String confirmPassword, Model model) {
		if (password.equals(confirmPassword)) {
			User u;
			u = service.findentierUser(username);
			u.setPass(password);
			service.updateUser(u);
			System.out.println(u.getPass());
			return "/admin/login";

		} else {
			model.addAttribute("me", "Password And ConfirmPassword Does Not match");
			model.addAttribute("mes", "Re-Enter the Username");
			return "admin/forgotpassword";

		}

	}

	/*
	 * Once the seller/admin login, It will navigate to the adminhome.
	 */
	@GetMapping("/admin/adminhome/{id}")
	public String adminhome(@PathVariable("id") int id, ModelMap model) {

		System.out.println(id);

		User u = service.getUsername(id);
		System.out.println(service.getUsername(id));
		model.addAttribute("add", u.getName());

		model.addAttribute("user", service.getuser(id));

		return "/admin/adminhome";
	}
	/*
	 * In adminhome, Seller/Admin have options to add new products, manage the
	 * existing products and updateprofile.
	 */

	@PostMapping("/admin/adminhome/{id}")
	public String adminhomepost(@PathVariable("id") int id) {

		ModelAndView view = new ModelAndView("admin/adminhome");
		// System.out.println(id);

		return "admin/adminhome";
	}

	/*
	 * If seller/admin wants to add new products, It will navigate to the
	 * addproducts page.
	 */

	@GetMapping("/admin/products/{id}")
	public String adminproducts(@PathVariable("id") int user_id, Model model) {
		model.addAttribute("user", service.getuser(user_id));

		return "admin/addproducts";
	}

	/*
	 * Seller/Admin enter the details of the products along with the image of
	 * the product.
	 */

	@PostMapping("/admin/products/{id}")
	public String adminadd(@RequestParam(name = "productName") String productName,
			@RequestParam(name = "eimage") MultipartFile file, @RequestParam(name = "price") double price,
			@RequestParam(name = "weight") float weight,
			@RequestParam(name = "productDescription") String productDescription,
			@RequestParam(name = "quantity") int quantity, @PathVariable("id") int user_id) throws IOException {

		byte[] byteArr = file.getBytes();
		String base64Encoded = new String(Base64.getEncoder().encode(byteArr));
		String s = "aaa";
		Products p = new Products(productName, price, weight, productDescription, quantity, base64Encoded, byteArr);
		p.setUserid(user_id);

		productservice.createProduct(p);

		System.out.println(productservice.getAllProducts());

		return "redirect:/admin/adminhome/{id}";
	}
	/*
	 * Seller/Admin can view the existing products added by that particular
	 * seller/admin.
	 */

	@GetMapping("/products/prolist/{id}")
	public String listpro(Model model, @PathVariable("id") int user_id) {

		model.addAttribute("user", service.getuser(user_id));

		model.addAttribute("Products", productservice.getAllproductsbyuser(user_id));

		return "products/prolist";

	}

	/*
	 * Seller/Admin can delete the existing product.
	 */

	@PostMapping("/products/prolist/{id}/{userid}")
	public String DeleteProduct(Model model, @PathVariable("id") int id, @PathVariable("userid") int user_id) {
		productservice.deleteProduct(id);
		return "redirect:/products/prolist/" + user_id;

	}

	/*
	 * If seller/admin wants to update the existing products Navigate to
	 * updateproduct page
	 */
	@GetMapping("/products/updateproduct/{id}")
	public String updateproduct(@PathVariable("id") int id, Model model) {

		model.addAttribute("product", productservice.getProduct(id));

		return "products/updateproduct";
	}

	/*
	 * Seller/Admin can Update the details of the existing products.
	 */

	@PostMapping("/products/updateproduct/{id}")
	public ModelAndView updateProduct(@PathVariable("id") int id, @ModelAttribute Products pro,
			@RequestParam("submit") String submit, Model model) {
		ModelAndView view = new ModelAndView("products/afterupdateprolist");

		Products p = productservice.getProduct(pro.getId());
		pro.setEimage(p.getEimage());
		pro.setImage(p.getImage());
		productservice.updateProduct(pro);
		pro.setUserid(uid);

		model.addAttribute("add", pro.getUserid());
		view.addObject("Products", productservice.getAllproductsbyuser(pro.getUserid()));

		return view;
	}

	/*
	 * If seller/admin wants to update the profile Navigate to updateprofile
	 * page.
	 */
	@GetMapping("/admin/updateProfile/{id}")
	public String adminUpdate(@PathVariable("id") int id, Model model) {
		model.addAttribute("user", service.getuser(id));
		return "/admin/updateProfile";
	}

	/*
	 * Seller/Admin can Update the profile.
	 */

	@PostMapping("/admin/updateProfile/{id}")
	public String adminUpdateInsert(@PathVariable("id") int id, @ModelAttribute User user, Model model) {
		System.out.println("SUCCESS");
		model.addAttribute("user", service.getuser(id));
		service.updateUser(user);

		return "redirect:/admin/adminhome/{id}";
	}

}
