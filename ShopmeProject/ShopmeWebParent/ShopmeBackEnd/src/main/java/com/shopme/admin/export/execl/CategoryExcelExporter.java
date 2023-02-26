package com.shopme.admin.export.execl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.admin.export.AbstractExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

public class CategoryExcelExporter extends AbstractExporter {
	
	
	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("Category");

	public void writeHeaderLine() {
		//sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont front = workbook.createFont();
		front.setBold(true);
		front.setFontHeight(16);
		cellStyle.setFont(front);

		createCell(row, 0, "Category ID", cellStyle);
		createCell(row, 1, "Category Name", cellStyle);

	}

	public void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex); // size chữ phù hợp vs text

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	public void export(List<Category> categories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "categories_");
       
		writeHeaderLine();
		writeDataLines(categories);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
    // ghi thông tin ra
	private void writeDataLines(List<Category> listCategories) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont front = workbook.createFont();
		front.setFontHeight(14);
		cellStyle.setFont(front);
		
		for(Category category : listCategories) {
			XSSFRow row = sheet.createRow(rowIndex++); 
			int columnIndex = 0;
			
			createCell(row, columnIndex++, category.getId(), cellStyle);
			createCell(row, columnIndex++, category.getName(), cellStyle);

		}
	}
}