package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;

public class CustomerUserDetailService implements UserDetailsService {
	
	@Autowired
	private CustomerRepository customerRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = customerRepo.findByEmail(email);
		if (customer != null) {
			return new CustomerUserDetails(customer);
		}
		throw new UsernameNotFoundException("Could not find customer with email : " + email);
	}

}
