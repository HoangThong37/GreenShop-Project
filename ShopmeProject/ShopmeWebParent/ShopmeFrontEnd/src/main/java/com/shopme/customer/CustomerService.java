package com.shopme.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;
import com.shopme.until.CustomerRegisterUtil;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {

	@Autowired 
	private CountryRepository countryRepo;
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}


	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	public void registerCustomer(Customer customer) {
		CustomerRegisterUtil.encodePassword(customer, passwordEncoder);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		customerRepo.save(customer);
		
	}
	
	// getCustomerEmail
	public Customer getCustomerEmail(String email) {
		return customerRepo.findByEmail(email);
	}
	
	public boolean verify(String verification) {
		Customer customer = customerRepo.findByVerificationCode(verification);
		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}
	
	public void updateAuthenticationType(AuthenticationType type, Customer customer) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}


	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode) {
		 Customer customer = new Customer();
		 customer.setEmail(email);
		 // customer.setFirstName(name);
		 setName(name, customer);
		 
		 customer.setEnabled(true);
		 customer.setCreatedTime(new Date());
		 customer.setAuthenticationType(AuthenticationType.GOOGLE);
		 customer.setPassword("");
		 customer.setAddressLine1("");
		 customer.setAddressLine2("");
		 customer.setCity("");
		 customer.setState("");
		 customer.setPhoneNumber("");  
		 customer.setPostalCode("");
		 customer.setCountry(countryRepo.findByCode(countryCode));
		
		 customerRepo.save(customer);
	}


	private void setName(String name, Customer customer) {
		String[] nameArrays = name.split(" ");
		if (nameArrays.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			String firstName = nameArrays[0];
			customer.setFirstName(firstName);
			
			String lastName = name.replaceFirst(firstName, "");
			customer.setLastName(lastName);
		}
		
	}
}