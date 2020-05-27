package com.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.data.Notification;
import com.presentation.MainWindow;
import com.src.model.ItemBean;
import com.src.model.ItemsAggregateBean;
import com.src.model.NotificationBean;
import com.src.model.NotificationsAggregateBean;

import java.util.Date;

import org.slf4j.*;

/**
 * @author imssbora
 */
@SpringBootApplication(scanBasePackages= "com")/*
@EntityScan(basePackages = {"com.src.repos", "org.axonframework.eventsourcing.eventstore.jpa",
		"org.axonframework.eventhandling.saga.repository.jpa", 
		 "org.axonframework.eventhandling.tokenstore.jpa"})*/
@EntityScan(basePackages = {"com.mypackage",
                            "org.axonframework.eventsourcing.eventstore.jpa",
                            //"org.axonframework.eventhandling.saga.repository.jpa",
                            "org.axonframework.modelling.saga.repository.jpa",
                            "org.axonframework.eventhandling.tokenstore.jpa",
                            "com.src.repos"})
@EnableJpaRepositories("com.src.repos")

public class WasteLessApplication implements CommandLineRunner{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
  //  @Autowired
   // NotificationRepository notRep;
    
   // @Autowired
   // ItemRepository itemRep;
    
    @Autowired
    WastelessController controller;
    
public static String removeKdigits(String num, int k) {
        
        int[] stack = new int[num.length()];
	//top of stack - tos = -1 => stack.isEmpty() 
        int tos = -1;
        
        int resultIndex = 0;

	//caz special
        
        if (num.length() == k) {
            return "0";
        }

        
        for (int j = 0; j < k; j ++) {
        	System.out.println("ITERTING FOR J = " + j);
            if (resultIndex >= num.length() || (tos != -1 && stack[tos] > (num.charAt(resultIndex) - 48))) {
             
                System.out.println("CONDITION 1 : RESULTINDEX = " + resultIndex + " STACK TOP = " + stack[tos] );
                tos --;
                continue;
            }
            while (resultIndex < num.length() - 1 && num.charAt(resultIndex) <= num.charAt(resultIndex + 1)) {
            	System.out.println("CONTITION 2 : RESULTINDEX = " + resultIndex + " ADDING " + num.charAt(resultIndex));
                stack[++tos] = (num.charAt(resultIndex++) - 48);
            }
            resultIndex ++;
        }
        while (resultIndex < num.length()) {
            stack[++tos] = (num.charAt(resultIndex++) - 48);
        }
                
        String result = "";
        while (tos > -1) {
            result = stack[tos--] + result;
        }
        
        int zeroes;
        String fixedResult = "";
        
        for(zeroes = 0; zeroes < result.length(); zeroes ++)
            if((result.charAt(zeroes) - 48) != 0)
                break;
        
        fixedResult = result.substring(zeroes);
        return (fixedResult.length() == 0) ? "0" : fixedResult;
    }
     
    public static void main(String[] args) {
    	//SpringApplicationBuilder builder = new SpringApplicationBuilder(WasteLessApplication.class);
    	//builder.headless(false);
    	//ConfigurableApplicationContext context = builder.run(args);
    	System.out.println(removeKdigits("789011", 3));
    	
    }
 
    public void run(String... args) throws Exception 
    {       
    	
    	//WebLookAndFeel.install(WebDarkSkin.class);
       
        
       MainWindow mw = new MainWindow();
       Utils utils = new Utils(mw, controller);
    	/*
    	NotificationBean notification = new NotificationBean();
    	notification.setMessage("MyNotification");
    	controller.addNotification(111, notification);
    	System.out.println(controller.getNotifications(111));
    	System.out.println(controller.getNotifications(111));
    	System.out.println(controller.getNotifications(111));
    	System.out.println(controller.getNotifications(111));
    	*/
    	//controller.deleteNotification(111, 1);

        /*
    	ItemsAggregateBean library = new ItemsAggregateBean();
    	library.setItemsAggregateId(111);
    	library.setName("myLibrary");
    	controller.addItemsAggregate(library);
    	ItemBean book = new ItemBean();
    	//book.setItemId(41);
    	book.setName("myOtherItem");
    	book.setCalories(300);
    	book.setQuantity(500);
    	book.setPurchase(new Date());
    	book.setExpiration(new Date());
    	//controller.addItem(111, book);
    	controller.deleteItem(111, 2);
    	System.out.println(controller.getItems(111));
    	System.out.println(controller.getItems(111));
    	System.out.println(controller.getItems(111));
    	System.out.println(controller.getItems(111));
    	*/
    }
}