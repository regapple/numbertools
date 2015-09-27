package com.song.order.listener;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class schduleContextListener
 *
 */
@WebListener
public class SchduleContextListener implements ServletContextListener {

	private final String SCHEDULE="schedle";
    /**
     * Default constructor. 
     */
    public SchduleContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        // TODO Auto-generated method stub
    	
    	ServletContext context=event.getServletContext();
		    
	    Scheduler scheduler = new Scheduler();
	    String path=context.getRealPath("/");
	    String separator=File.separator;
	    
	    if(!path.endsWith(separator)){
	        path+=separator;
	    }
	    path=path+"WEB-INF"+separator+"classes"+separator+"schedule.txt";
	    
	    File f=new File(path);
	    scheduler.scheduleFile(f);
	    
	    context.setAttribute(SCHEDULE, scheduler);
	    //System.out.println("start schduel context ok....");
	    scheduler.start();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        // TODO Auto-generated method stub
    	ServletContext context=event.getServletContext();
		Scheduler scheduler =(Scheduler) context.getAttribute(SCHEDULE);
		
		context.removeAttribute(SCHEDULE);
		
		//scheduler.stop();
    }
	
}
