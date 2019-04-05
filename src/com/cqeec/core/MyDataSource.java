package com.cqeec.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import com.cqeec.util.GlobalParams;

public class MyDataSource {
	private LinkedList<Connection> dataSources=new LinkedList<Connection>();
	public MyDataSource() {
		try {
			Class.forName(GlobalParams.properties.getProperty("driver"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   init();
		 } 
	public void init() {
		try {
			for (int i = 0; i < 50; i++) {
				Connection con;
				con = DriverManager.getConnection(GlobalParams.properties.getProperty("url"),GlobalParams.properties.getProperty("username") ,GlobalParams.properties.getProperty("password"));
				dataSources.add(con); 
				} 
			 } 
		       catch (Exception e) {
				    e.printStackTrace(); 
				}  
	}
	
	
	       //取出连接池中一个连接 
			public synchronized Connection getConnection(){
				if(dataSources.size()==0)init();
				final Connection conn = dataSources.removeFirst(); 
				// 删除第一个连接返回 
				return conn; 
			} //将连接放回连接池 
			public synchronized void releaseConnection(Connection conn) {
				dataSources.add(conn); 
			}
	}
