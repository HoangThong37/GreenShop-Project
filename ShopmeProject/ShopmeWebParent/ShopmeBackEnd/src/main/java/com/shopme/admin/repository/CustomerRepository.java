package com.shopme.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;

@Repository
public interface CustomerRepository extends SearchRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);
	
//	@Query("UPDATE Customer c SET c.enabled = true WHERE c.id = ?1")
//	@Modifying // Annotation @Modifying để cập nhật dữ liệu
//	public void enable(Integer id);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying // Annotation @Modifying để cập nhật dữ liệu	
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
			+ "c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state, ' ', "
			+ "c.postalCode, ' ', c.country.name) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
	
	
	// findAll - Page<Customer>
	// findByEmail(email)
	// updateEnabledStatus()
	// countById(id)
	// findAll - Page<Customer>
	// phân trang

}
