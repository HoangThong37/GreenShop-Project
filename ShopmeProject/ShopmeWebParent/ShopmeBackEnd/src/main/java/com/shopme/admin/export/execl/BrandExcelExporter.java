package com.shopme.admin.export.execl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

public class BrandExcelExporter extends AbstractExporter {
	
	
	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("Brand");

	public void writeHeaderLine() {
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont front = workbook.createFont();
		front.setBold(true);
		front.setFontHeight(16);
		cellStyle.setFont(front);

		createCell(row, 0, "Brand Id", cellStyle);
		createCell(row, 1, "Brand Name", cellStyle);
		createCell(row, 2, "Categories of Brand", cellStyle);
	}

	public void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex); // size chữ phù hợp vs text

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		}  if (value instanceof Set){
			String text = "";
			Iterator itr = ((Set) value).iterator();
		    while (itr.hasNext()) {
		        text += " " + itr.next();
		    }
		    cell.setCellValue(text);
		}
		cell.setCellStyle(style);	
	}

	public void export(List<Brand> brands, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "brands_");
       
		writeHeaderLine();
		writeDataLines(brands);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
    // ghi thông tin ra
	private void writeDataLines(List<Brand> listCategories) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont front = workbook.createFont();
		front.setFontHeight(14);
		cellStyle.setFont(front);
		
		for(Brand brand : listCategories) {
			XSSFRow row = sheet.createRow(rowIndex++); 
			int columnIndex = 0;
			
			createCell(row, columnIndex++, brand.getId(), cellStyle);
			createCell(row, columnIndex++, brand.getName(), cellStyle);
			createCell(row, columnIndex++, brand.getCategories(), cellStyle);
		}
	}
}
//
//hasNext(): có phần tử tiếp theo hay không
//next(): lấy phần tử tiếp theo
//remove(): loại bò phần tử cuối cùng