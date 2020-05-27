package com.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

enum ReportType{
	WEEKLY, MONTHLY;
}

public abstract class Report {
	
	private Date creationDate;
	private ReportType type = null;
	private int totalItems = 0;
	private int totalCalories = 0;
	private int expiredItems = 0;
	private float wastedCalories = 0;
	private int purchasedItems = 0;
	private Date firstDay = null;
	
	public Report(ReportType type, List<Item> items, Date firstDay) {
		this.type = type;
		this.firstDay = firstDay;
		this.creationDate = new Date(System.currentTimeMillis());

		
		Iterator <Item> it = items.iterator();
		
		while(it.hasNext()) {
			Item currItem = it.next();
			totalItems += currItem.getQuantity();
			totalCalories += currItem.getCalories();
			wastedCalories += this.getIdealBurndownRatio(currItem);
			if(currItem.getExpiration().compareTo(new Date(System.currentTimeMillis())) < 0) {
				expiredItems += currItem.getQuantity();
			}
			if(currItem.getPurchase().compareTo(firstDay) > 0) {
				purchasedItems ++;
			}
			
		}		
		
		try {
			this.generatePDF();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private float getIdealBurndownRatio(Item item) {
		
		long diffInMillies = Math.abs(item.getExpiration().getTime() - (new Date(System.currentTimeMillis())).getTime());
	    int daysUntilExpiration = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    
	    return ((float) item.getCalories() / (float)(daysUntilExpiration + 1)) * item.getQuantity();
	}
	
	abstract void construct();
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public ReportType getType() {
		return type;
	}
	public void setType(ReportType type) {
		this.type = type;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public int getTotalCalories() {
		return totalCalories;
	}
	public void setTotalCalories(int totalCalories) {
		this.totalCalories = totalCalories;
	}
	public int getExpiredItems() {
		return expiredItems;
	}
	public void setExpiredItems(int expiredItems) {
		this.expiredItems = expiredItems;
	}
	public float getWastedCalories() {
		return wastedCalories;
	}
	public void setWastedCalories(int wastedCalories) {
		this.wastedCalories = wastedCalories;
	}
	
	public void generatePDF() throws DocumentException, FileNotFoundException{
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
		 
		document.open();
		Paragraph title = new Paragraph();
		
		switch(type) {
			case WEEKLY:
				title.add("Weekly WasteLess Report");
				break;
			
			case MONTHLY:
				title.add("Monthly WasteLess Report");
				break;
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		
		Paragraph dates = new Paragraph();

		
		switch(type) {
		
			case WEEKLY:
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				dates.add(formatter.format(new Date(cal.getTimeInMillis() + 86400000)));
				break;
		
			case MONTHLY:
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dates.add(formatter.format(new Date(cal.getTimeInMillis())));
				break;
		}
		
		dates.add(" - " + formatter.format(new Date(System.currentTimeMillis())));
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
        title.setAlignment(Element.ALIGN_CENTER);
        dates.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph message = new Paragraph();
        message.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph message2 = new Paragraph();
        message2.add("Total count of grocery items: " + totalItems);
        message2.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message3 = new Paragraph();
        message3.add("Total calorie count of owned items: " + totalCalories);
        message3.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message4 = new Paragraph();
        message4.add("Total number of expired items: " + expiredItems);
        message4.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message5 = new Paragraph();
        message5.add("Total burned calories per day: " + df.format(wastedCalories));
        message5.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message6 = new Paragraph();
        message6.add("Total purchased items: " + purchasedItems);
        message6.setAlignment(Element.ALIGN_LEFT);
        
        switch(type) {
			case WEEKLY:
				message.add("Current week: ");
				break;
		
		case MONTHLY:
			message.add("Current month: ");
			break;
        }
              
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(message);
        document.add(Chunk.NEWLINE);
        document.add(dates);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(message2);
        document.add(message3);
        document.add(message4);
        document.add(message5);
        document.add(message6);
		document.close();
	}
}
