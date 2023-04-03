package com.shopme.admin.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	
	@Autowired
	public CustomerRepository repo;
	
	@Autowired
	public TestEntityManager entityManager;
	
	@Test
	public void testCreateCustomer() {
		Integer countryId = 5; // mỹ
		Country country = entityManager.find(Country.class, countryId); // return entity
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("David");
		customer.setLastName(" Beckham");
		customer.setPassword("thongbem");
		customer.setEmail("beckham@gmail.com");
		customer.setPhoneNumber("098-878-888");
		customer.setAddressLine1("1372 west ham");
		customer.setCity("Sacsamento");
		customer.setState("California");
		customer.setPostalCode("3222");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		assertThat(savedCustomer).isNotNull(); // Kiểm tra giá trị không phải là `null`
		assertThat(savedCustomer.getId()).isGreaterThan(0); // Kiểm tra giá trị lớn hơn giá trị mong muốn.
	}
	
	
	@Test
	public void testCreateCustomer0() {
		Integer countryId = 6; // mỹ
		Country country = entityManager.find(Country.class, countryId); // return entity
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("San");
		customer.setLastName(" Da");
		customer.setPassword("thongbem");
		customer.setEmail("sanda11@gmail.com");
		customer.setPhoneNumber("094-438-342");
		customer.setAddressLine1("123, A-, Shar & Nahar India.estate, Sunmill Road");
		customer.setAddressLine2("Daharai, A-, Lower Parel");
		customer.setCity("Mumbai");
		customer.setState("maharasa");
		customer.setPostalCode("21556");
		customer.setCreatedTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		assertThat(savedCustomer).isNotNull(); // Kiểm tra giá trị không phải là `null`
		assertThat(savedCustomer.getId()).isGreaterThan(0); // Kiểm tra giá trị lớn hơn giá trị mong muốn.
	}
	
	@Test
	public void testListCustomer() {
		List<Customer> listCustomer = (List<Customer>) repo.findAll();
		listCustomer.forEach(System.out::println);
		
		assertThat(listCustomer.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer id = 1;
		String name = "StankMusk";
		Customer customer = repo.findById(id).get();
		customer.setFirstName(name);
		customer.setEnabled(false);
		Customer updateCustomer = repo.save(customer);
		assertThat(updateCustomer.getFirstName()).isEqualTo(name);	// `isEqualTo()`: Kiểm tra giá trị bằng với giá trị mong muốn.
	}	
	
	@Test
	public void testGetCustomer() {
		Integer id = 1;
		Optional<Customer> findById = repo.findById(id);		
		assertThat(findById.isPresent());			
		
		Customer customer = findById.get();
		System.out.println(customer);
		// assertThat(customer).isNotNull();		
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer id = 3;
		repo.deleteById(id);
		Optional<Customer> findById = repo.findById(id);
		assertThat(findById).isNotPresent();		
	}
	
	@Test
	public void testFindByEmail() {
		String email = "sanda11@gmail.com";
		Customer customerByEmail = repo.findByEmail(email);
		assertThat(customerByEmail).isNotNull();
		System.out.println(customerByEmail);
				
	}
	
	@Test
	public void testFindByVerification() {
		String code = "code_123";
		Customer customer = repo.findByVerificationCode(code);
		assertThat(customer).isNotNull();
		System.out.println(customer);
				
	}
	
//	@Test
//	public void testEnabled() {
//		Integer idInteger = 2;
//		repo.enable(idInteger);
//		
//		Customer customer = repo.findById(idInteger).get();
//		assertThat(customer.isEnabled()).isTrue();
//	//	System.out.println(customer);		
//	}

}
