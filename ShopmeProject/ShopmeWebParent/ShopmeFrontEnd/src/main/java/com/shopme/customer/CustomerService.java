package com.shopme.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class CustomerService {
	
	@Autowired
	private CountryRepository countryRepo; // quốc gia
	
	@Autowired
	private CustomerRepository customerRepo; // khách hàng
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	// lấy tất cả quốc gia
	public List<Country> listAllCountry() {
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}
	
	// register customer
	public void registerCustomer(Customer customer) {
		encodePassword(customer); // mã hóa password
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode); // set mã xác nhận
		
		System.out.println("Verification Code : " + randomCode);
		
	}

	private void encodePassword(Customer customer) {
		String encodedPasword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPasword);
		
	}
	
	

}
