package com.shopme.admin.export.brand;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.export.AbstractExporter;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

public class BrandCsvExporter extends AbstractExporter {
	public void export(List<Brand> brands, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv",  ".csv", "brand_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Brand ID", "Brand Name"};
		String[] fieldMapping = {"id", "name"};
		csvWriter.writeHeader(csvHeader);
		// add list category
		for (Brand brand : brands) {
			brand.setName(brand.getName());
			csvWriter.write(brand, fieldMapping);
		}
		csvWriter.close();
	}
}
