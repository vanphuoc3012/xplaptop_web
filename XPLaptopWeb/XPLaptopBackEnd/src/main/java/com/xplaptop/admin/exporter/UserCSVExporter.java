package com.xplaptop.admin.exporter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.xplaptop.common.entity.user.User;

public class UserCSVExporter extends AbstractExporter {

	public void export(List<User> list, HttpServletResponse response) throws IOException {	
		String[] csvHeader = {"User ID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
		String[] nameMapping = {"id", "email", "firstName", "lastName", "Roles", "enabled"};
		super.setHttpHeader(response, "users_", AbstractExporter.CSV_EXTENSION, AbstractExporter.CSV_CONTENTTYPE);
		
		ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);			
		writer.writeHeader(csvHeader);
		for(User t : list) {
			writer.write(t, nameMapping);
		}		
		writer.close();
	}
	
	
}
