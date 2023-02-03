package com.shopme.admin.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.shopme.common.entity.User;

public class AbstractExporter {
	//
	public void setResponseHeader(HttpServletResponse response, String contentType, String extension,
			String prefix) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"); // format
		String timestamp = dateFormat.format(new Date());
		String fileName = prefix + timestamp + extension;

		response.setContentType(contentType);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; fileName=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
}
