package com.shopme.admin.export.brand;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.export.AbstractExporter;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

public class BrandPdfExporter extends AbstractExporter {

	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf", "brands_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        
        Paragraph paragraph = new Paragraph("List of brands", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        
        document.add(paragraph);
	   
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {
        		1.2f, 3.5f, 3.0f
        });
        
        writeTableHeader(table); // header cho pdf
        writeTableData(table, listBrands); //  data pdf
        
        document.add(table);
		document.close();
	}

	private void writeTableData(PdfPTable table, List<Brand> brands) {
		// valueOf() được sử dụng để chuyển đối kiểu dữ liệu khác thành chuỗi
		for(Brand brand : brands) {
			table.addCell(String.valueOf(brand.getId()));
			table.addCell(brand.getName());
			table.addCell(brand.getCategories().toString());
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        
        cell.setPhrase(new Phrase("Brand ID", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Brand Name", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Categories of Brand", font));
        table.addCell(cell);
	}

}
