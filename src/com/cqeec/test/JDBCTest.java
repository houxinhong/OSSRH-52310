package com.cqeec.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.cqeec.core.DBUtil;
public class JDBCTest {

	/**
	 * 测试ResultSetMetaData
	 * @throws SQLException
	 */
	@Test
	public void test01() throws SQLException {
      ResultSetMetaData metaData=DBUtil.getConn().prepareStatement("select * from permission").executeQuery().getMetaData();
      System.out.println(metaData.getColumnCount());
      System.out.println(metaData.getColumnName(1));
	}
	
	/**
	 * 插入后获取自增主键id
	 * @throws SQLException
	 */
	@Test
	public void test02() throws SQLException {
		PreparedStatement ps=DBUtil.getConn().prepareStatement("insert into permission(name)values('132')");
		ps.execute();
		ResultSet rs=ps.executeQuery("SELECT @@IDENTITY");
		rs.next();
		System.out.println(rs.getInt(1));
	}
	
	@Test
	public void test03() throws SQLException {
		PreparedStatement ps=DBUtil.getConn().prepareStatement("insert into test(id,name)values('sda','132')");
		ps.execute();
		ResultSet rs=ps.executeQuery("SELECT @@IDENTITY");
		rs.next();
		System.out.println(rs.getString(1));
    }
	
	
}
