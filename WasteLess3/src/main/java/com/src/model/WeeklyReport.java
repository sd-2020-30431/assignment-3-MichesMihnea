package com.src.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeeklyReport extends Report {

	
	public WeeklyReport(List <ItemBean> items, Date firstDay) {
		
		super(ReportType.WEEKLY, items, firstDay);
	}

	@Override
	void construct() {
		
		
	}


}
