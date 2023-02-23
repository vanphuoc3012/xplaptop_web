package com.xplaptop.admin.exporter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.xplaptop.common.entity.user.User;

public class UserPDFExporter extends AbstractExporter{
	
	public void export(List<User> listUsers, HttpServletResponse response) throws DocumentException, IOException {
		super.setHttpHeader(response, "users_", ".pdf", "application/pdf");
		
		String[] pdfFirstRow = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fontTitle.setSize(20);
		
		Paragraph header = new Paragraph("List of User of XPLaptop Website\n", fontTitle);
		header.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(header);
		
		PdfPTable usersTable = new PdfPTable(6);
		
		usersTable.setWidthPercentage(100f);
		usersTable.setWidths(new int[] {1, 3, 3, 3, 5, 2});
		usersTable.setSpacingBefore(5);
		
		PdfPCell cell = new PdfPCell();
		Font firstRowFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
		for(int i = 0; i < pdfFirstRow.length; i++) {
			cell.setPhrase(new Phrase(pdfFirstRow[i], firstRowFont));
			usersTable.addCell(cell);
		}
		
		for(User user : listUsers) {
			usersTable.addCell(String.valueOf(user.getId()));
			usersTable.addCell(user.getEmail());
			usersTable.addCell(user.getFirstName());
			usersTable.addCell(user.getLastName());
			usersTable.addCell(user.getRoles().toString());
			usersTable.addCell(String.valueOf(user.isEnabled()));
			
		}
		
		document.add(usersTable);
		document.close();
	}

}
