package test;

import java.awt.geom.Area;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.cqeec.pojo.Role;
import com.cqeec.pojo.RoleMapper;
import com.cqeec.pojo.RoleMapper.Condition;
import com.cqeec.pojo.User;
import com.cqeec.util.DBUtil;
import com.cqeec.util.GlobalParams;
import com.cqeec.util.SqlUtil;
import com.cqeec.util.StringUtil;
import com.mysql.fabric.xmlrpc.base.Data;

public class SqlTest {
	/**
	 * 下标测试
	 * @throws SQLException
	 */
	@Test
	public void test01() throws SQLException {
		Connection con=DBUtil.getConn();
		System.out.println(SqlUtil.getDeleteSql(Role.class));
		PreparedStatement ps=con.prepareStatement(SqlUtil.getDeleteSql(Role.class)+"where id=?");
		System.out.println(SqlUtil.getDeleteSql(Role.class)+"where id=?");
		ps.setLong(1,1000);
		ps.execute();
	}
	
	/**
	 * 顺序测试
	 */
	@Test
	public void test02() {
		Field[] fields=User.class.getDeclaredFields();
		for(Field field:fields) {
			System.out.println(field.getName());
		}
	}
	
	
	/**
	 * 参数类型测试
	 * @throws SQLException
	 */
	@Test
	public void test03() throws SQLException {
		Connection con=DBUtil.getConn();
		System.out.println(SqlUtil.getDeleteSql(Role.class));
		PreparedStatement ps=con.prepareStatement("select * from user where name=?");
		System.out.println(SqlUtil.getDeleteSql(Role.class)+"where id=?");
		ps.setObject(1,"zhang3");
		System.out.println(ps.executeQuery().next());
	}
	
	@Test
	public void test04() {
		System.out.println(SqlUtil.getInsertSql(User.class));
	}
	
	@Test
	public void test05() {
		RoleMapper mapper=new RoleMapper();
		Condition condition1=mapper.createCondtion();
		List<Role> roles=mapper.selectByCondition(condition1);
		for(Role role:roles) {
			System.out.println(role);
		}
	}
	
	@Test
	public void test06() {
		RoleMapper.enabletransaction();
		RoleMapper roleMapper=new RoleMapper();
		roleMapper.delete(2);
        RoleMapper.commit();		
		for(Role role:roleMapper.selectByCondition(null)) {
			System.out.println(role);
		}
	}
	
	@Test
	public void test07() throws SQLException {
		
		Connection connection=DBUtil.getConn();
		System.out.println(connection.getAutoCommit());
		connection.setAutoCommit(false);
		System.out.println(connection.getAutoCommit());
		Map<String, String> a=new HashMap<>();
		System.out.println(a.remove("1"));
		
	}
	
	
	
	
}
