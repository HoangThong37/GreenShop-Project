package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Customer;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerService service;
	
	@PostMapping("customers/check_unique_email")
	public String checkEmail(@Param("email") String email) {
		
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}

}
