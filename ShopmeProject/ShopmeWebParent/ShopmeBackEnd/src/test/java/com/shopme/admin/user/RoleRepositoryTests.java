package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.service.spi.Manageable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.RoleRepository;
import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role admin = new Role("admin", "manager everything");
		Role saveRole = repo.save(admin);
		   
		assertThat(saveRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Salesperson", "Manageable product price, " 
				     + "customers, shipping, orders and sales report");
		
		Role roleEditor = new Role("Editor", "manage categories, brands, " 
			     + "products , articles, orders and sales report");
		
		Role roleShippeRole = new Role("Shipper", "view product, view orders, " 
			     + "and update order status");
		
		Role roleAssistant = new Role("Assistant", "manage question and reviews");
		repo.saveAll(List.of(roleSalesperson, roleEditor, roleShippeRole));
		
		  
	}
	
	
	
}
