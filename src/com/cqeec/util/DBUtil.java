package com.cqeec.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.cqeec.bean.Configuration;
import com.cqeec.core.MyDataSource;


public class DBUtil {
	private static Map<Thread,Connection> userConnection=new HashMap<>();
	
    private static MyDataSource dataSource;
	private static Configuration conf;
	//加载配置文件
	private static void  loadProperties(){  //静态代码块
		Properties pros = GlobalParams.properties;
		conf = new Configuration();
		conf.setDriver(pros.getProperty("driver"));
		conf.setTargetPackage(pros.getProperty("targetPackage"));
		conf.setPassword(pros.getProperty("password"));
		conf.setTargetProject(new File(pros.getProperty("targetProject")).getAbsolutePath());
		conf.setUrl(pros.getProperty("url"));
		conf.setUsername(pros.getProperty("username"));
		conf.setDatabase(pros.getProperty("database"));
	}
	
	
	//开启事务
	public static void enableTransaction() {
		if(userConnection.get(Thread.currentThread())==null) {
			userConnection.put(Thread.currentThread(), getConn());
		}
		try {
			getConn().setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//关闭事物
	public static void commitTransaction() {
		if(userConnection.get(Thread.currentThread())!=null) {
			try {
				getConn().commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	//对外提供使用获取连接释放连接的方法
	public static Connection getConn() {
		//根据是否使用连接池进行对象的获取
		if(GlobalParams.properties.getProperty("datasource").equals("mydatasource")) {
			if(userConnection.get(Thread.currentThread())!=null) {
				return userConnection.get(Thread.currentThread());
			}else {
				//向连接池获取一个连接就将少一个连接（以下方式真正做到一次请求一个连接）
				Connection connection=getConnBySelfPoll();
				//将获取到的连接放置到用户连接map中,为了本次请求多次访问数据库
				userConnection.put(Thread.currentThread(), connection);
				return connection;
			}
		}
		//其他连接池
		
		
		//不使用连接池
		else{
			if(userConnection.get(Thread.currentThread())!=null) {
				return userConnection.get(Thread.currentThread());
			}else {
				//向连接池获取一个连接就将少一个连接（以下方式真正做到一次请求一个连接）
				Connection connection=getConnByJdbc();
				//将获取到的连接放置到用户连接map中,为了本次请求多次访问数据库
				userConnection.put(Thread.currentThread(), connection);
				return connection;
			}
		}
	}
	
	public static void close() {
		//根据是否使用连接池进行对象的获取
		if(GlobalParams.properties.getProperty("datasource").equals("mydatasource")) {
			if(userConnection.get(Thread.currentThread())!=null) {
				dataSource.releaseConnection(userConnection.get(Thread.currentThread()));
			}
		}
		//其他连接池
		
		else {
			//不使用连接池
			if(userConnection.get(Thread.currentThread())!=null) {
				closeByJdbc(userConnection.get(Thread.currentThread()));
			}
			
		}
		userConnection.remove(Thread.currentThread());
		
	}
	
	/**
	 * 使用自己的连接池获取连接
	 */
	private static Connection getConnBySelfPoll() {
		if(dataSource==null)dataSource=new MyDataSource();
		return dataSource.getConnection();
	}
	/**
	 * 使用自己的连接池关闭连接
	 */
	public static void closeBySlefPool(Connection connection) {
		dataSource.releaseConnection(connection);
	}
	
	
	
	/**
	 * 不使用连接池获取连接
	 * @return
	 */
	private static Connection getConnByJdbc(){
		   if(conf==null)loadProperties();
		try {
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(),
					conf.getUsername(),conf.getPassword());     //直接建立连接，后期增加连接池处理，提高效率！！！
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *不使用连接池关闭连接
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	private static void closeByJdbc(Object... objs){
		if(objs!=null) {
			try {
				for(Object object:objs) {
					if(object!=null&&object instanceof ResultSet){
						((ResultSet)object).close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				for(Object object:objs) {
					if(object!=null&&object instanceof PreparedStatement){
						((ResultSet)object).close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				for(Object object:objs) {
					if(object!=null&&object instanceof Connection){
						((ResultSet)object).close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回Configuration对象
	 * @return
	 */
	public static Configuration getConf(){
		if(conf==null)loadProperties();
		return conf;
	}

	
	

	
}
