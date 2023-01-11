package com.shopme.admin.user.export;

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

import com.shopme.admin.user.AbstractExporter;
import com.shopme.common.entity.User;

public class UserExcelExporter extends AbstractExporter {
	
	
	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet = workbook.createSheet("Users");

	public void writeHeaderLine() {
		//sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont front = workbook.createFont();
		front.setBold(true);
		front.setFontHeight(16);
		cellStyle.setFont(front);

		createCell(row, 0, "User ID", cellStyle);
		createCell(row, 1, "Email", cellStyle);
		createCell(row, 2, "First Name", cellStyle);
		createCell(row, 3, "Last Name", cellStyle);
		createCell(row, 4, "Roles", cellStyle);
		createCell(row, 5, "Enabled", cellStyle);

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

	public void export(List<User> users, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx");
        writeHeaderLine();
		writeDataLines(users);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void writeDataLines(List<User> listUsers) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont front = workbook.createFont();
		front.setFontHeight(14);
		cellStyle.setFont(front);
		
		for(User user : listUsers) {
			XSSFRow row = sheet.createRow(rowIndex++); 
			int columnIndex = 0;
			
			createCell(row, columnIndex++, user.getId(), cellStyle);
			createCell(row, columnIndex++, user.getEmail(), cellStyle);
			createCell(row, columnIndex++, user.getFirstName(), cellStyle);
			createCell(row, columnIndex++, user.getLastName(), cellStyle);
			createCell(row, columnIndex++, user.getRoles().toString(), cellStyle);
			createCell(row, columnIndex++, user.isEnabled(), cellStyle);
		}
	}
}
/*
 * Apache POI cung cấp cho bạn các interface Workbook, Sheet, Row, Cell,… và các
 * class thể hiện (implementation) tương ứng:
 * 
 * Workbook: 
 * đại diện cho một file Excel. Nó được triển khai dưới hai class là:
 * HSSFWorkbook và XSSFWorkbook tương ứng cho định dạng .xls và .xlsx
 * 
 * Sheet:
 * đại diện cho một bảng tính Excel (một file Excel có thể có nhiều Sheet). Nó
 * có 2 class là HSSFSheet và XSSFSheet. Row: đại diện cho một hàng trong một
 * bảng tính (Sheet). Nó có 2 class là HSSFRow và XSSFRow. 
 * 
 * Cell: đại diện cho
 * một ô trong một hàng (Row). Tương tự nó cũng có 2 class là HSSFCell and
 * XSSFCell.
 */
// https://viblo.asia/p/huong-dan-doc-va-ghi-file-excel-trong-java-su-dung-thu-vien-apache-poi-RQqKLENpZ7z