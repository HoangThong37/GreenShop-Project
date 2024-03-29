package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopme.customer.CustomerUserDetailService;
import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomerOAuth2UserService oAuth2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginHandler;
	
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginHandler;
	
	@Autowired 
	public WebSecurityConfig(CustomerOAuth2UserService oAuth2UserService, OAuth2LoginSuccessHandler oauth2LoginHandler,
			DatabaseLoginSuccessHandler databaseLoginHandler) {
		super();
		this.oAuth2UserService = oAuth2UserService;
		this.oAuth2LoginHandler = oauth2LoginHandler;
		this.databaseLoginHandler = databaseLoginHandler;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/account_details", "/update_account_details").authenticated()
		.anyRequest().permitAll()
		.and()
		.formLogin()
			.loginPage("/login")
			.usernameParameter("email")
			.successHandler(databaseLoginHandler)
			.permitAll()
		.and() 
		.oauth2Login()
		   .loginPage("/login")
		   .userInfoEndpoint()
		   .userService(oAuth2UserService)
		   .and()
		   .successHandler(oAuth2LoginHandler)
		   //.and()
		.and()
		.logout().permitAll()
		.and()
		.rememberMe()
			.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
			.tokenValiditySeconds(14 * 24 * 60 * 60)
		;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailService();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}	

	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
// 
//	@Override
//	protected void configure(HttpSecurity http) throws Exception { 
//		http.authorizeRequests()
//		.antMatchers("/customer").authenticated()
//		.anyRequest().permitAll()
//		.and()
//		.formLogin()
//		   .loginPage("/login")
//		   .usernameParameter("email")
//		   .permitAll()
//		 .and()
//		 .logout().permitAll()
//		 .and().rememberMe()
//          // .key("1234567890_abcdefghiklmnopqrse")
//		 .tokenValiditySeconds(14 * 24 * 60 * 60);
//		
//		//http.authorizeRequests().anyRequest().permitAll();	 
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
//	}
//	
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//		
//		return authProvider;
//	}
//	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new CustomerUserDetailService();
//	}
//	//4:02
 
}
