package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.SettingService;
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
}


//	@Autowired
//	private CustomerService customerService;
//
//	@Autowired
//	private SettingService settingService;
//
//	@GetMapping("/register")
//	public String showRegisterCustomer(Model model) {
//		List<Country> listCountries = customerService.listAllCountry();
//
//		model.addAttribute("listCountries", listCountries);
//		model.addAttribute("pageTitle", "Customer Registration");
//		model.addAttribute("customer", new Customer());
//
//		return "register/register_form";
//	}
//
//	// create customer
//	@PostMapping("/create_customer")
//	public String createCustomer(HttpServletRequest request, Customer customer, Model model)
//			throws UnsupportedEncodingException, MessagingException {
//		customerService.registerCustomer(customer);
//		sendVerificationEmail(request, customer);
//
//		model.addAttribute("pageTitle", "Registration Succeeded");
//		
//		return "/register/register_success";
//	}
//	
//	
//
//	private void sendVerificationEmail(HttpServletRequest request, Customer customer)
//			throws UnsupportedEncodingException, MessagingException {
//		EmailSettingBag emailSettingBag = settingService.getEmailSettings();
//		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag); // send mail
//
//		String toAddress = customer.getEmail();
//		String subject = emailSettingBag.getCustomerVerifySubject();
//		String content = emailSettingBag.getCustomerVerifyContent();
//
//		MimeMessage messageMail = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(messageMail);
//
//		helper.setFrom(emailSettingBag.getFromAddres(), emailSettingBag.getSenderName());
//		helper.setTo(toAddress);
//		helper.setSubject(subject);
//
//		content = content.replace("[[name]]", customer.getFullName()); // cũ -> mới
//		String verifyURL = Utility.getSiteUrl(request) + "/verify?code=" + customer.getVerificationCode();
//		content = content.replace("[[URL]]", verifyURL);
//		
//		helper.setText(content, true);
//		mailSender.send(messageMail);
//		System.out.println("To Address = " + toAddress);
//		System.out.println("Verify URL = " + verifyURL);
//	}
//}
