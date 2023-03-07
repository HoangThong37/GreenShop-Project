package com.shopme.setting;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.Setting;

@Component
public class SettingFilter implements Filter {
	
	@Autowired
	private SettingService service;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String url = servletRequest.getRequestURI().toString();
		
		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".css")
				                 || url.endsWith(".jpg")) {
			chain.doFilter(request, response);
			return;
		}  
		List<Setting> genaralSettings = service.getGeneralSetting();
		genaralSettings.forEach(item -> {
			System.out.println(item);
			request.setAttribute(item.getKey(), item.getValue());
		});
		
		//System.out.println(url);
		chain.doFilter(request, response);
	}
	
}
