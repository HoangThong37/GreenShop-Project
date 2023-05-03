package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.SettingService;
import com.shopme.until.CustomerAccountUtil;
import com.shopme.until.CustomerRegisterUtil;

@Controller
public class CustomerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired 
	private CustomerService customerService;
	
	@Autowired 
	private SettingService settingService;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
	
		List<Country> listCountries = customerService.listAllCountries();

		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());

		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		customerService.registerCustomer(customer);
		CustomerRegisterUtil.sendVerificationEmail(request, customer, settingService);

		model.addAttribute("pageTitle", "Registration Succeeded!");

		return "/register/register_success";
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code) {
     	boolean verify = customerService.verify(code);
     	return "/register/" + (verify ? "verify_success" : "verfiy_fail");
	}
	
	
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {

		String email = CustomerAccountUtil.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(email);

		List<Country> listCountries = customerService.listAllCountries();

		model.addAttribute("customer", customer);
		model.addAttribute("listCountries", listCountries);

		return "customer/account_form";
	}

	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra,
			HttpServletRequest request) {

		customerService.update(customer);

		ra.addFlashAttribute("message", "Your account details have been updated.");

		CustomerAccountUtil.updateNameForAuthenticatedCustomer(customer, request);

		return "redirect:/account_details";
	}
}
