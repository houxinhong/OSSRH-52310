package com.cqeec.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.cqeec.util.CollectionUtil;
import com.cqeec.util.SqlUtil;

public class RoleMapper {
	//常用的增删改查
	public void insert(Role role) {
	        String sql=SqlUtil.getInsertSql(role.getClass()); 
	        SqlUtil.save(sql,role);
	}
	
	public void delete(long id) {
			String sql=SqlUtil.getDeleteSql(Role.class)+"where id=?";
			SqlUtil.delete(sql,id);
	}
	
	public void update(Role role) {
		String sql=SqlUtil.getUpdateSql(role.getClass());
		SqlUtil.modify(sql,CollectionUtil.sortByUpdate(role));
		
	}
	
	public Role select(long id) {
		String sql=SqlUtil.getSelectSql(Role.class, "where id=?");
		return SqlUtil.select(sql,Role.class,id)!=null?(Role)SqlUtil.select(sql,Role.class,id).get(0):null;
	}
	//批量增删改查
    public void batchInsert(List<Role> roles) {
		
	}
	
	public void batchDelete(long[] ids) {
		
	}
	
	public void batchUpdate(List<Role> roles) {
		
	}
	
	public Role batchSelect(long[] ids) {
		
		return null;
	}
	//根据条件删查
	public void deleteByCondition(Condition condition) {
		 List<Role> list=selectByCondition(condition);
		 for(Role role:list) {
			 delete(role.getId());
		 }
	}
	
	public List<Role> selectByCondition(Condition condition) {
		
		return null;
	}
	
	//以sql语句进行增删该查--但不推荐使用
	@Deprecated
    public void insertBySql(String sql) {
		
	}
	@Deprecated
	public void deleteBySql(String sql) {
		
	}
	@Deprecated
	public void updateBySql(String sql) {
		
	}
	@Deprecated
	public List<Role> selectBySql(String sql) {
		return null;
	}
	
	
	
	public static class Condition{
		
	}
	
	
}
