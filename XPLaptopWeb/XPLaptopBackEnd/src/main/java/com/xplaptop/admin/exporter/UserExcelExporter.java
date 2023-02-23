package com.xplaptop.admin.exporter;

import com.xplaptop.common.entity.user.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {	
		super.setHttpHeader(response, "users_", ".xlsx", "application/vnd.ms-excel");
		String[] excelHeaders = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
		
		try (Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
			Sheet sheet = workbook.createSheet("Sheet1");
			
			Row headerRow = sheet.createRow(0);
			for(int col = 0; col < excelHeaders.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(excelHeaders[col]);
				
			}
			
			int contentRow = 1;
			for(User user : listUsers) {
				Row row = sheet.createRow(contentRow);
				contentRow++;
				
				row.createCell(0).setCellValue(user.getId());
				row.createCell(1).setCellValue(user.getEmail());
				row.createCell(2).setCellValue(user.getFirstName());
				row.createCell(3).setCellValue(user.getLastName());
				row.createCell(4).setCellValue(user.getRoles().toString());
				row.createCell(5).setCellValue(user.isEnabled());
			}
			
			workbook.write(response.getOutputStream());
			workbook.close();
		} catch (IOException e) {
			throw new IOException("Fail to export PDF file");
		}
	}
}
