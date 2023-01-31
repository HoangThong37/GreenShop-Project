package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.user.export.AbstractExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

public class CategoryCsvExporter extends AbstractExporter {
	public void export(List<Category> categories, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv",  ".csv", "categories_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Category ID", "Category Name"};
		String[] fieldMapping = {"id", "name"};
		csvWriter.writeHeader(csvHeader);
		// add list category
		for (Category category : categories) {
			category.setName(category.getName().replace("--"," "));
			csvWriter.write(category, fieldMapping);
		}
		csvWriter.close();
	}
}
