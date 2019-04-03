package test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

import com.cqeec.pojo.Role;
import com.cqeec.pojo.RoleMapper;
import com.cqeec.pojo.User;
import com.cqeec.util.DBUtil;
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
		//mapper.delete(4);
		//mapper.insert(role);
		//mapper.update(role);
		//Role role=new Role("7", Long.parseLong(8+""), "2");
		System.out.println(mapper.select(1));
	}
	
	
	
	
}
