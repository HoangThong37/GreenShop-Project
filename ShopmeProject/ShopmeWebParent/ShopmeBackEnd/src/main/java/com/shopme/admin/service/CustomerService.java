package com.shopme.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.repository.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;


@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 5;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private CountryRepository countryRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// listByPage() - Page<Customer>
	// updateCustomerEnabledStatus
	// get(id)
	// isEmailUnique(id,email)
	// + save 
	// delete(id)
	
	
	// page
	public void listByPage(int pageNumber, PagingAndSortingHelper helper) {
         helper.listEntities(pageNumber, CUSTOMERS_PER_PAGE, customerRepo);
	}
	
	// listAllCountries
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}
    
	// save - encode - setPasswword
	public void save(Customer customer) {
		Customer customerId = customerRepo.findById(customer.getId()).get();
		
        if (!customer.getPassword().isEmpty()) {
			String encodePassword = passwordEncoder.encode(customer.getPassword());
			customer.setPassword(encodePassword);
		} else {
			customer.setPassword(customerId.getPassword());
		}
        customer.setEnabled(true);
        customer.setCreatedTime(customerId.getCreatedTime());
        customer.setVerificationCode(customerId.getVerificationCode());
        customer.setAuthenticationType(customerId.getAuthenticationType());
        
		customerRepo.save(customer);
	}
	
	// kiểm tra email
	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);

		if (existCustomer != null && existCustomer.getId() != id) {
			return false;
		}
		return true;
	}

	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
	      customerRepo.updateEnabledStatus(id, enabled);
     }


	public void delete(Integer id) throws CustomerNotFoundException {
		Long idDeleted = customerRepo.countById(id);  // countById
		if (idDeleted == null || idDeleted == 0) {
			throw new CustomerNotFoundException("Could not find any customer with ID : " + id);
		}
		customerRepo.deleteById(id);
	}
	
	// get theo id
	public Customer get(Integer id) throws CustomerNotFoundException{
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any customer with ID : " + id);
		}
	}

	public List<Customer> listAll() {
	  
	    Sort firstNameSort = Sort.by("id").ascending();
		List<Customer> listCustomer = new ArrayList<>();
		customerRepo.findAll(firstNameSort).forEach(listCustomer::add);
		
		return listCustomer;
	}
	
}
