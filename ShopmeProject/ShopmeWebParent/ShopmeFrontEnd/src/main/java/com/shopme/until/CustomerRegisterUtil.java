package com.shopme.until;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopme.common.entity.Customer;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

public class CustomerRegisterUtil {
	// private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegisterUtil.class);

	public static void encodePassword(Customer customer, PasswordEncoder passwordEncoder) {

		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		
		customer.setPassword(encodedPassword);
	}
	
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();

		return siteURL.replace(request.getServletPath(), "");
	}

	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());

		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

		mailSender.setJavaMailProperties(mailProperties);

		return mailSender;
	}
	
	public static void sendVerificationEmail(HttpServletRequest request, Customer customer, SettingService settingService) 
			throws UnsupportedEncodingException, MessagingException {

		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		JavaMailSenderImpl mailSender = prepareMailSender(emailSettings);

		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();

		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
     	content = content.replace("[[name]]", customer.getFullName());

		String verifyURL = getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);

	}
	
}
