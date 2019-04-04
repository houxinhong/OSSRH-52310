package test;

import java.awt.geom.Area;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.cqeec.pojo.Role;
import com.cqeec.pojo.RoleMapper;
import com.cqeec.pojo.RoleMapper.Condition;
import com.cqeec.pojo.User;
import com.cqeec.util.DBUtil;
import com.cqeec.util.GlobalParams;
import com.cqeec.util.SqlUtil;

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
		RoleMapper mapper=new RoleMapper();
		Condition condition=mapper.createCondtion();
		Condition condition2=mapper.createCondtion();
		List<Object> list=new ArrayList<>();
		condition.andDesc_Like("%员%").limit(0, 2).orderBy("id",false);
		
		List<Role> roles=mapper.selectByCondition(condition);
		for(Role role:roles) {
			System.out.println(role);
		}
	}
	
	@Test
	public void test07() {
		Properties properties=GlobalParams.properties;
		System.out.println(properties.get("url"));
	}
	
	
	
	
}
