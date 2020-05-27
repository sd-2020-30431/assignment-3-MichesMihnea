package com.business;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.dom4j.DocumentException;

import com.src.model.MonthlyReport;
import com.src.model.NotificationBean;
import com.src.model.RedReport;
import com.src.model.Report;
import com.src.model.WeeklyReport;
import com.presentation.DonateFoodWindow;
import com.presentation.MainWindow;
import com.src.model.GreenReport;
import com.src.model.ItemBean;


public class Utils {
	private MainWindow mw;
	private List <String> messages = new ArrayList <String> ();
	private List <String> names = new ArrayList <String> ();
	private List <Integer> quantities = new ArrayList <Integer> ();
	private List <Integer> calories = new ArrayList <Integer> ();
	private List <Date> purchaseDates = new ArrayList <Date> ();
	private List <Date> expirationDates = new ArrayList <Date> ();
	private List <ItemBean> itemsToRemove = new ArrayList <ItemBean> ();
	
	private WastelessController controller;
	private int calorieGoal = 2000;
	private int aggregateId = 111;
	
	public Utils(MainWindow mw, WastelessController controller) {
		this.mw = mw;
		this.controller = controller;
		mw.nglw.btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				names = mw.nglw.getNames();
				quantities = mw.nglw.getQuantities();
				calories = mw.nglw.getCalories();
				purchaseDates = mw.nglw.getPurchaseDates();
				expirationDates = mw.nglw.getExpirationDates();
				
				Iterator <String> namesIterator = names.iterator();
				Iterator <Integer> quantitiesIterator = quantities.iterator();
				Iterator <Integer> caloriesIterator = calories.iterator();
				Iterator <Date> purchaseDatesIterator = purchaseDates.iterator();
				Iterator <Date> expirationDatesIterator = expirationDates.iterator();
				
				while(namesIterator.hasNext()) {
					ItemBean newItemBean = new ItemBean();
					newItemBean.setName(namesIterator.next());
					newItemBean.setQuantity(quantitiesIterator.next());
					newItemBean.setCalories(caloriesIterator.next());
					newItemBean.setPurchase(purchaseDatesIterator.next());
					newItemBean.setExpiration(expirationDatesIterator.next());
					
					controller.addItem(aggregateId, newItemBean);
				}
				
			}
		});
		mw.btnViewGroceries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List <ItemBean> items = Utils.this.getItemBeans();
				names = new ArrayList <String> ();
				quantities = new ArrayList <Integer> ();
				calories = new ArrayList <Integer> ();
				purchaseDates = new ArrayList <Date> ();
				expirationDates = new ArrayList <Date> ();
				
				Iterator <ItemBean> it = items.iterator();
				
				while(it.hasNext()) {
					ItemBean currentItemBean = it.next();
					names.add(currentItemBean.getName());
					quantities.add(currentItemBean.getQuantity());
					calories.add(currentItemBean.getCalories());
					purchaseDates.add(currentItemBean.getPurchase());
					expirationDates.add(currentItemBean.getExpiration());
				}
				
				mw.vgw.setNames(names);
				mw.vgw.setQuantities(quantities);
				mw.vgw.setCalories(calories);
				mw.vgw.setPurchaseDates(purchaseDates);
				mw.vgw.setExpirationDates(expirationDates);
				
				mw.vgw.display();
			}
		});
		
		mw.btnViewNotifications.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Utils.this.checkStatus();
				List <NotificationBean> notifications = Utils.this.getNotifications();
				messages = new ArrayList <String> ();
				
				Iterator <NotificationBean> it = notifications.iterator();
				
				while(it.hasNext()) {
					NotificationBean currentNotification = it.next();
					messages.add(currentNotification.getMessage());
				}
				
				mw.vnw.setMessages(messages);
				mw.vnw.display();
			}
		});
		
		mw.btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int newGoal = mw.getNewCalorieGoal();
				if(newGoal != -1)
					Utils.this.calorieGoal = newGoal;
			}
		});
		
		mw.btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List <String> options = Utils.this.getNamesAndQuantities();
				List <ItemBean> currentItemBeansState = Utils.this.getItemBeans();
				itemsToRemove = new ArrayList <ItemBean> ();
				mw.dfw = new DonateFoodWindow(options);
				mw.dfw.setVisible(true);
				mw.dfw.addWindowListener(new WindowAdapter(){
	                public void windowClosing(WindowEvent e){
	                	
	                	}
				});
				mw.dfw.btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(mw.dfw.getSelectedIndex() != -1) {
							ItemBean selectedItemBean = currentItemBeansState.get(mw.dfw.getSelectedIndex());
							options.remove(mw.dfw.getSelectedIndex());
							currentItemBeansState.remove(mw.dfw.getSelectedIndex());
							itemsToRemove.add(selectedItemBean);
							mw.dfw.setVisible(false);
							mw.dfw = new DonateFoodWindow(options);
							mw.dfw.btnNewButton.addActionListener(this);
							mw.dfw.btnNewButton_1.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									if(Utils.this.itemsToRemove.size() != 0) {
										Iterator <ItemBean> it = itemsToRemove.iterator();
										while(it.hasNext()) {
											ItemBean currItemBean = it.next();
											controller.deleteItem(aggregateId, currItemBean.getItemId());
										}
										
										mw.dfw.dispatchEvent(new WindowEvent(mw.dfw, WindowEvent.WINDOW_CLOSING));
									}
								}
							});
							mw.dfw.setVisible(true);
							
							Iterator <ItemBean> it = itemsToRemove.iterator();
							while(it.hasNext()) {
								ItemBean currItemBean = it.next();
								mw.dfw.addToTable(currItemBean.getName(), currItemBean.getQuantity());
							}
						}
					}
				});
				mw.dfw.btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(Utils.this.itemsToRemove.size() != 0) {
							Iterator <ItemBean> it = itemsToRemove.iterator();
							while(it.hasNext()) {
								ItemBean currItemBean = it.next();
								System.out.println("DELETING " + currItemBean.getItemId());
								controller.deleteItem(aggregateId, currItemBean.getItemId());
							}
							
							mw.dfw.dispatchEvent(new WindowEvent(mw.dfw, WindowEvent.WINDOW_CLOSING));
						}
					}
				});
			}
		});
		
		mw.btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//ReportFactory.generateReport(Utils.this.getItemBeans(), mw.getComboBoxIndex());
				
				Report report = null;
				
				if(mw.getComboBoxIndex() == 0) {
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.clear(Calendar.MINUTE);
					cal.clear(Calendar.SECOND);
					cal.clear(Calendar.MILLISECOND);
					cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
					
					report = new WeeklyReport(Utils.this.getItemBeans(), new Date(cal.getTimeInMillis()));
				}else {
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.clear(Calendar.MINUTE);
					cal.clear(Calendar.SECOND);
					cal.clear(Calendar.MILLISECOND);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					
					report = new MonthlyReport(Utils.this.getItemBeans(), new Date(cal.getTimeInMillis()));
				}
				
				List <ItemBean> items = Utils.this.getItemBeans();
				
				float calorieDiff = 0;
				
				Iterator <ItemBean> it = items.iterator();
				float totalDailyCalories = 0;
				
				while(it.hasNext()) {
					ItemBean currItemBean = it.next();
					if(currItemBean.getExpiration().compareTo(new Date(System.currentTimeMillis() + 86400000 * 2)) < 0 && currItemBean.getExpiration().compareTo(new Date(System.currentTimeMillis())) > 0) {
					
					}
					if(currItemBean.getExpiration().compareTo(new Date(System.currentTimeMillis())) < 0){

					}
					else totalDailyCalories += Utils.this.getIdealBurndownRatio(currItemBean);
				}
				
					calorieDiff = totalDailyCalories - Utils.this.calorieGoal;
					
					System.out.println("CALORIE DIFF : " + calorieDiff);
				
					if(calorieDiff > 0){
						RedReport finalReport = new RedReport(report);
						finalReport.setFont();
						finalReport.writeReport();
					}else {
						GreenReport finalReport = new GreenReport(report);
						finalReport.setFont();
						finalReport.writeReport();
					}
				
			}
		});
		
	}
	
	private void checkStatus() {
		
		Utils.this.wipe();
		
		List <ItemBean> items = Utils.this.getItemBeans();
		
		Iterator <ItemBean> it = items.iterator();
		float totalDailyCalories = 0;
		
		while(it.hasNext()) {
			ItemBean currItemBean = it.next();
			if(currItemBean.getExpiration().compareTo(new Date(System.currentTimeMillis() + 86400000 * 2)) < 0 && currItemBean.getExpiration().compareTo(new Date(System.currentTimeMillis())) > 0) {
				NotificationBean newNotification = new NotificationBean();
				newNotification.setMessage("ItemBean " + currItemBean.getName() + " is about to expire!");
				NotificationBean newNotification2 = new NotificationBean();
				newNotification2.setMessage("Please consider donating " + currItemBean.getName());
				controller.addNotification(aggregateId, newNotification);
				controller.addNotification(aggregateId, newNotification2);
			}
			if(currItemBean.getExpiration().compareTo(new Date(System.currentTimeMillis())) < 0){
				NotificationBean newNotification = new NotificationBean();
				newNotification.setMessage("ItemBean " + currItemBean.getName() + " has expired!");
				controller.addNotification(aggregateId, newNotification);
			}
			else totalDailyCalories += Utils.this.getIdealBurndownRatio(currItemBean);
		}
		
		if(this.calorieGoal < totalDailyCalories) {
			NotificationBean newNotification = new NotificationBean();
			newNotification.setMessage("There is a risk of having waste! Excess daily calories: " + (totalDailyCalories - this.calorieGoal));
			controller.addNotification(aggregateId, newNotification);
		}
		
	}
	
	private float getIdealBurndownRatio(ItemBean item) {
		
		long diffInMillies = Math.abs(item.getExpiration().getTime() - (new Date(System.currentTimeMillis())).getTime());
	    int daysUntilExpiration = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    
	    return ((float) item.getCalories() / (float)(daysUntilExpiration + 1)) * item.getQuantity();
	}
	
	public List<ItemBean> getItemBeans() {
		try {
			return controller.getItems(aggregateId);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List <String> getNamesAndQuantities() {
		List <String> itemNames = new ArrayList <String> ();
		Iterator <ItemBean> it = this.getItemBeans().iterator();
		
		while(it.hasNext()) {
			ItemBean currItemBean = it.next();
			itemNames.add(currItemBean.getName() + "(" + currItemBean.getQuantity() + ")");
		}
		
		return itemNames;
	}
	
	public List <NotificationBean> getNotifications(){
		List <NotificationBean> notifications = new ArrayList <NotificationBean> ();
		
		try {
			notifications = controller.getNotifications(aggregateId);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notifications;
	}
	
	public void wipe() {
		List <NotificationBean> notifications = new ArrayList <NotificationBean> ();
		
		try {
			notifications = controller.getNotifications(aggregateId);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator <NotificationBean> it = notifications.iterator();
		
		while(it.hasNext()) {
			controller.deleteNotification(aggregateId, it.next().getNotificationId());
		}
	}

}
