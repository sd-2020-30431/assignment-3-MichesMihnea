package com.data;

import java.util.Date;
import java.util.List;

public class MonthlyReport extends Report {
	
	public MonthlyReport(List <Item> items, Date firstDay) {
		super(ReportType.MONTHLY, items, firstDay);
	}

	@Override
	void construct() {
		// TODO Auto-generated method stub

	}

}
