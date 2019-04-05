package com.cqeec.core;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import com.cqeec.util.DBUtil;

public class MyServletRequestListener implements ServletRequestListener {
     @Override
     public void requestDestroyed(ServletRequestEvent sre) {
          DBUtil.close();
     }
     @Override
     public void requestInitialized(ServletRequestEvent sre) {
     }
 }