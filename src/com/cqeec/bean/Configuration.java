package com.cqeec.bean;

/**
 * 管理配置信息
 * @author gaoqi www.sxt.cn
 *
 */
public class Configuration {
	/**
	 * 驱动类
	 */
	private String driver; 
	/**
	 * jdbc的url
	 */
	private String url;
	/**
	 * 数据库的用户名
	 */
	private String username;
	/**
	 * 数据库的密码
	 */
	private String password;
	/**
	 * 正在使用哪个数据库
	 */
	private String database;
	/**
	 * 项目的源码路径
	 */
	private String targetProject;
	/**
	 * 扫描生成java类的包(po的意思是：Persistence object持久化对象)
	 */
	private String targetPackage;
	public Configuration(String driver, String url, String username, String password, String database,
			String targetProject, String targetPackage) {
		super();
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.database = database;
		this.targetProject = targetProject;
		this.targetPackage = targetPackage;
	}
	public Configuration() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getTargetProject() {
		return targetProject;
	}
	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}
	public String getTargetPackage() {
		return targetPackage;
	}
	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

}