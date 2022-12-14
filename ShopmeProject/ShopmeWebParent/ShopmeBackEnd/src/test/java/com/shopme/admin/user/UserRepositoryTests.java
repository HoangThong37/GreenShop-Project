package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role role = entityManager.find(Role.class, 1);
		User userThong = new User("thong@gmail.com", "12345", "Bem", "Nguyễn");
		userThong.addRole(role);
		
		User userSaved = userRepo.save(userThong);
		assertThat(userSaved.getId()).isGreaterThan(0); // assertThat là một trong các phương thức JUnit từ đối tượng Assert 
		                                                // có thể được sử dụng để kiểm tra xem một giá trị cụ thể có khớp với giá trị dự kiến ​​hay không.
		 
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userBem = new User("bem11@gmail.com", "bem123", "Bem", "Nguyễn");
		Role roleEditor = new Role(3);
		// Role roleAdmin = new Role(1);
		Role roleAssistant = new Role(5);
		
		userBem.addRole(roleEditor);
		userBem.addRole(roleAssistant);
		
		User saveUser = userRepo.save(userBem);
		assertThat(saveUser.getId()).isGreaterThan(0);
                 
	}
	// Test List All User
	@Test
	public void testListAllUser() {
		Iterable<User> listUsers = userRepo.findAll();
		listUsers.forEach(list -> System.out.println(list));
		// assertThat(listUsers.toString()).isGreaterThan(0);        
	}
	
	
	// Test get user by Id
	@Test
	public void getUserById() {
	 User userNam = userRepo.findById(1).get();
	 System.out.println(" Test :  " + userNam);
	 assertThat(userNam).isNotNull();
	}
	
	// Test get user by Id
	@Test
	public void testUpdateUserDetails() {
	 User userThong = userRepo.findById(1).get();
	 userThong.setEnabled(true);
	 userThong.setEmail("thong.it@gmail.com");
	 
	 userRepo.save(userThong);
//	 assertThat(userThong).isNotNull();
	}
	
	// update user roles
	@Test
	public void testUpdateUserRoles() {
	 User userThong = userRepo.findById(2).get();
	 Role roleEditor = new Role(3);
	 Role roleSalesperson = new Role(2);
	 
	 userThong.getRoles().remove(roleEditor);
	 userThong.addRole(roleSalesperson);
	 userRepo.save(userThong);
	}
	
	// update user roles
	@Test
	public void deleteUser() {
	 userRepo.deleteById(2);
	 userRepo.findById(2);
	
	}
	
}
