package com.xplaptop.admin.exporter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractExporter {
	
	protected final static String PDF_EXTENSION = ".pdf";
	protected final static String PDF_CONTENTTYPE = "application/pdf";
	protected final static String EXCEL_EXTENSION = ".xlsx";
	protected final static String EXCEL_CONTENTTYPE = "application/vnd.ms-excel";
	protected final static String CSV_EXTENSION = ".csv";
	protected final static String CSV_CONTENTTYPE = "text/csv";
			
	public static void setHttpHeader(HttpServletResponse response,
									String prefix,
									String extension,
									String contentType) {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-MM-ss");
		String fileName = prefix+dateTime.format(formatter)+extension;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
	}
}
