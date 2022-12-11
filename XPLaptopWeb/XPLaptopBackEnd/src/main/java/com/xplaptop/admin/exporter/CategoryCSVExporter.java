package com.xplaptop.admin.exporter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.xplaptop.common.entity.Category;

public class CategoryCSVExporter extends AbstractExporter {
	
	public void export(List<Category> list, HttpServletResponse response) throws IOException {	
		String[] csvHeader = {"Category ID", "Name", "Alias", "Enable Status"};
		String[] nameMapping = {"id", "name", "alias", "enabled"};
		super.setHttpHeader(response, "categories_", AbstractExporter.CSV_EXTENSION, AbstractExporter.CSV_CONTENTTYPE);
		
		ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);			
		writer.writeHeader(csvHeader);
		for(Category c : list) {
			c.setName(c.getName().replace("-", "  "));
			writer.write(c, nameMapping);
		}		
		writer.close();
	}
}
