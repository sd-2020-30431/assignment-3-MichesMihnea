package com.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeeklyReport extends Report {

	
	public WeeklyReport(List <Item> items, Date firstDay) {
		
		super(ReportType.WEEKLY, items, firstDay);
	}

	@Override
	void construct() {
		
		
	}


}
