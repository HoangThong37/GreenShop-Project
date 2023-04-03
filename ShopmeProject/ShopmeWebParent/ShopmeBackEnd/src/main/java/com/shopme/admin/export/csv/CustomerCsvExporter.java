package com.shopme.admin.export.csv;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.export.AbstractExporter;
import com.shopme.common.entity.Customer;

public class CustomerCsvExporter extends AbstractExporter {
	public void export(List<Customer> customers, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response, "text/csv",  ".csv", "customer_");

		// CsvBeanWriter writes a CSV file by mapping each
		//               field on the bean to a column in the CSV file (using the supplied name mapping).
		Writer writer = new OutputStreamWriter(response.getOutputStream(), "utf-8");
		writer.write('\uFEFF');
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = { "Customer ID", "First Name", "Last Name", "E-mail", "City", "State", "Country", "Enabled"};
		String[] fieldMapping = {"id", "firstName", "lastName", "email", "city", "state", "country", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		// add list customer
		for (Customer customer : customers) {
			csvWriter.write(customer, fieldMapping);
		}
		csvWriter.close();
	}
}
