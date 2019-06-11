package com.cqeec.core;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestEvent;

import javax.servlet.ServletRequestListener;

public class MyListener implements ServletRequestListener,ServletContextListener {
     @Override
     public void requestDestroyed(ServletRequestEvent sre) {
          DBUtil.close();
     }
     @Override
     public void requestInitialized(ServletRequestEvent sre) {
    	 
     }
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		GlobalParams.setPath("config.properties");
	}
     
 }