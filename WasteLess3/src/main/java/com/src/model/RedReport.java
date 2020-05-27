package com.src.model;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

public class RedReport extends ReportDecorator{
	
	public RedReport(Report report) {
		super(report);
	}
	
	public void setFont() {
		super.setFont(new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED));
	}

	@Override
	void construct() {
		// TODO Auto-generated method stub
		
	}
	
	public void writeReport() {
		this.setFont();
		System.out.println("IN DECORATORRED");
		super.generateReport();
	}

}
