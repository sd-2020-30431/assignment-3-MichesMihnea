package com.data;

import java.util.List;

public class ReportFactory {
	
	private ReportFactory() {
		
	}
	
	public static void generateReport(List <Item> items, int type) {
	
	switch(type) {
	
		case 0:
			WeeklyReportFactory.generateReport(items);
			break;
			
		case 1:
			MonthlyReportFactory.generateReport(items);
			break;
	}
	
	}
}
