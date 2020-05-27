package com.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthlyReportFactory {

		public static void generateReport(List <Item> items) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			Report report = new MonthlyReport(items, new Date(cal.getTimeInMillis()));
		}
}
