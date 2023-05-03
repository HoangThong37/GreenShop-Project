package com.shopme.until;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.common.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;

public class CustomerAccountUtil {

public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
			
		Object principal = request.getUserPrincipal();

		String customerEmail = null;
		

		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();

			customerEmail = oauth2User.getEmail();
		}

		return customerEmail;
	}
	
	public static void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		
		Object principal = request.getUserPrincipal();
		
		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			
			Customer authenticatedCustomer = userDetails.getCustomer();

			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());

		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();

			String fullName = customer.getFirstName() + " " + customer.getLastName();

			oauth2User.setFullName(fullName);
		}		
	}

	public static CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		
		CustomerUserDetails userDetails = null;
		
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		} else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}

		return userDetails;
	}
}