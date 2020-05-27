package com.src.model;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;

import com.itextpdf.text.Font;

abstract class ReportDecorator extends Report {
	
	public Report report;

	public ReportDecorator(Report report) {
		this.report = report;
	}
	
	protected void setFont(Font font) {
		report.setFont(font);
	}
	
	public abstract void setFont();
	
	public void generateReport() {
		System.out.println("IN DECORATOR");
		try {
			report.generatePDF();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
