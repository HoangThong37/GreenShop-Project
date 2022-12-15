package com.shopme.admin.user;


//import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncoderPassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = "thong123";
		String encoderPassword = passwordEncoder.encode(password);
		
		System.out.println(encoderPassword);
		boolean matches = passwordEncoder.matches(password, encoderPassword);
		
//		assertThat(matches).isTrue();
		
	}
}
