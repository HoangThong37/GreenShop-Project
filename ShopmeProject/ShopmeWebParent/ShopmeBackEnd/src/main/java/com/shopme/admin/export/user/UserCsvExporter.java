package com.shopme.admin.export.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

public class UserCsvExporter {

	public void export(List<User> users, HttpServletResponse response) throws IOException {
		// super.setResponseHeader(response, "text/csv", ".csv");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"); // format
		String timestamp = dateFormat.format(new Date());
		String fileName = "users_" + timestamp + ".csv";

		response.setContentType("text/csv");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; fileName=" + fileName;
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "User ID", "Email", "First Name", "Last Name", "Roles", "Enabled" };
		String[] mapUser = { "id", "email", "firstName", "lastName", "roles", "enabled" };
		csvWriter.writeHeader(csvHeader);
		// add list user
		for (User user : users) {
			csvWriter.write(user, mapUser);
		}
		csvWriter.close();
	}
	// https://biquyetxaynha.com/huong-dan-spring-boot-excel-khoi-dong-mua-xuan-excel
}
