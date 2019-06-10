package com.cqeec.test;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.cqeec.util.DBUtil;
public class JDBCTest {

	@Test
	public void test01() throws SQLException {
      ResultSetMetaData metaData=DBUtil.getConn().prepareStatement("select * from permission").executeQuery().getMetaData();
      System.out.println(metaData.getColumnCount());
      System.out.println(metaData.getColumnName(1));
	}
	
}
