package com.shopme.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.service.CustomerService;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService customerService;
	
	
	// checkDuplicateEmail(email)
}
