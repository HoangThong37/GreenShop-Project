package com.shopme.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Customer;

public class CustomerUserDetails implements UserDetails {
	
	private Customer customer;
	

	public CustomerUserDetails(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return customer.isEnabled();
	}
	
	public String getFullName() {
		return customer.getFirstName() + " " + customer.getLastName();
	}

}
