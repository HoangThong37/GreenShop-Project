package com.shopme;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.setting.EmailSettingBag;

public class Utility {
	
	public static String getSiteUrl(HttpServletRequest request) {
		String siteUrl  = request.getRequestURL().toString();
		
		return siteUrl.replace(request.getServletPath(), "");
	}
	 // cbi gửi thư
	// dùng thư viện Java Mail
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(null);
		mailSender.setPort(0);
		mailSender.setUsername(null);
		mailSender.setPassword(null);
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		return mailSender;
	}

}
