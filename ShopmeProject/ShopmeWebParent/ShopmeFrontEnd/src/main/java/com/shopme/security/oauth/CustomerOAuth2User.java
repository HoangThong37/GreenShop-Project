package com.shopme.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {
	
	private String clientName;
	private OAuth2User oAuth2User;
	private String fullName;

	public CustomerOAuth2User(String clientName, OAuth2User oAuth2User) {
		super();
		this.clientName = clientName;
		this.oAuth2User = oAuth2User;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oAuth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oAuth2User.getAttribute("name");
	}

	public String getEmail() {
		return oAuth2User.getAttribute("email");
	}

	public String getFullName() {
		return fullName != null ? fullName : oAuth2User.getAttribute("name");
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	

}
