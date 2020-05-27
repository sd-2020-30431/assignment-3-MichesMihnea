package com.src.model;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dom4j.DocumentException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
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
	private Font font = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	
	protected void setFont(Font font) {
		this.font = font;
	}
	
	public Report() {
		
	}
	
	public Report(ReportType type, List<ItemBean> items, Date firstDay) {
		this.type = type;
		this.firstDay = firstDay;
		this.creationDate = new Date(System.currentTimeMillis());

		
		Iterator <ItemBean> it = items.iterator();
		
		while(it.hasNext()) {
			ItemBean currItem = it.next();
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
	
	private float getIdealBurndownRatio(ItemBean item) {
		
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
		System.out.println("FONT IS : " + font.getColor().toString());
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
		} catch (FileNotFoundException | com.itextpdf.text.DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		document.open();
		Chunk titleChunk = null;
		
		switch(type) {
			case WEEKLY:
				titleChunk = new Chunk("Weekly Wasteless Report", font);
				break;
			
			case MONTHLY:
				titleChunk = new Chunk("Monthly Wasteless Report", font);
				break;
		}
		
		Paragraph title = new Paragraph(titleChunk);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		
		Chunk datesChunk = null;
		
		switch(type) {
		
			case WEEKLY:
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				datesChunk = new Chunk(formatter.format(new Date(cal.getTimeInMillis() + 86400000)), font);
				break;
		
			case MONTHLY:
				cal.set(Calendar.DAY_OF_MONTH, 1);
				datesChunk = new Chunk(formatter.format(new Date(cal.getTimeInMillis())), font);
				break;
		}
		
		Paragraph dates = new Paragraph(datesChunk);

		Chunk datesChunk2 = new Chunk(formatter.format(new Date(System.currentTimeMillis())), font);
		
		Paragraph dates2 = new Paragraph(datesChunk2);
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
        title.setAlignment(Element.ALIGN_CENTER);
        dates.setAlignment(Element.ALIGN_CENTER);
        dates2.setAlignment(Element.ALIGN_CENTER);
        
        Chunk chunk2 = new Chunk("Total count of grocery items: " + totalCalories, font);
        Paragraph message2 = new Paragraph(chunk2);
        message2.setAlignment(Element.ALIGN_LEFT);
        
        Chunk chunk3 = new Chunk("Total calorie count of owned items: " + totalCalories, font);
        Paragraph message3 = new Paragraph(chunk3);
        message2.setAlignment(Element.ALIGN_LEFT);
        
        Chunk chunk4 = new Chunk("Total number of expired items: " + expiredItems, font);
        Paragraph message4 = new Paragraph(chunk4);
        message2.setAlignment(Element.ALIGN_LEFT);
        
        Chunk chunk5 = new Chunk("Total burned calories per day: " + df.format(wastedCalories), font);
        Paragraph message5 = new Paragraph(chunk5);
        message2.setAlignment(Element.ALIGN_LEFT);
        
        Chunk chunk6 = new Chunk("Total purchased items: " + purchasedItems, font);
        Paragraph message6 = new Paragraph(chunk6);
        message2.setAlignment(Element.ALIGN_LEFT);
        
        Chunk chunk = null;
        
        switch(type) {
			case WEEKLY:
				chunk = new Chunk("Current week: ", font);
				break;
		
		case MONTHLY:
			chunk = new Chunk("Current month: ", font);
			break;
        }
        
        Paragraph message = new Paragraph(chunk);
        message.setAlignment(Element.ALIGN_CENTER);
              
        try {
        	
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(message);
        document.add(Chunk.NEWLINE);
        document.add(dates);
        document.add(dates2);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(message2);
        document.add(message3);
        document.add(message4);
        document.add(message5);
        document.add(message6);
        
        }catch(Exception e) {
        	e.printStackTrace();
        }
		document.close();
	}
}
