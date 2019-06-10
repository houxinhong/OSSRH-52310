package com.cqeec.core;
import javax.servlet.ServletRequestEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

import javax.servlet.ServletRequestListener;

import com.cqeec.util.DBUtil;
import com.cqeec.util.GlobalParams;

public class MyListener implements ServletRequestListener,ApplicationListener<ContextStartedEvent> {
     @Override
     public void requestDestroyed(ServletRequestEvent sre) {
          DBUtil.close();
     }
     @Override
     public void requestInitialized(ServletRequestEvent sre) {
    	 
     }
     @Override
 	public void onApplicationEvent(ContextStartedEvent event) {
          //这里设置配置文件的路径
    	 GlobalParams.setPath("config.properties");
 	}
     
 }