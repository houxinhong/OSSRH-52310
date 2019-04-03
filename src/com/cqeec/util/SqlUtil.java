package com.cqeec.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class SqlUtil {
	/**
	 * 插入语句的sql
	 * @param clazz
	 * @return
	 */
	public static String getInsertSql(Class clazz) {
		StringBuffer sb=new StringBuffer();
		sb.append("insert into ");
		//获取表名
		String tableName=TableUtil.getTableNameByClass(clazz);
		sb.append(tableName+" ");
		//获取问号数量
		sb.append("values(");
		Field[] fields=clazz.getDeclaredFields();
        for(Field field:fields) {
        	sb.append("?,");
        }
		sb.replace(sb.lastIndexOf("?,"),sb.length(),"?");
		sb.append(")");
		return sb.toString();
	};
	
	/**
	 * 删除的sql
	 * @param clazz
	 * @return
	 */
	public static String getDeleteSql(Class clazz) {
		StringBuffer sb=new StringBuffer();
		sb.append("delete from ");
		//获取表名
		String tableName=TableUtil.getTableNameByClass(clazz);
		sb.append(tableName+" ");
		return sb.toString();
	}

	/**
	 * 更新语句的sql
	 * @param clazz
	 * @return
	 */
	public static String getUpdateSql(Class clazz) {
		StringBuffer sb=new StringBuffer();
		sb.append("update ");
		//获取表名
		String tableName=TableUtil.getTableNameByClass(clazz);
		sb.append(tableName+" ");
		//其余数据
		Field[] fields=clazz.getDeclaredFields();
        for(Field field:fields) {
        	if(!field.getName().equals("id")) {
        		sb.append("set ");
            	sb.append(field.getName());
            	sb.append("=");
            	sb.append("?,");
        	}
        }
        sb.replace(sb.lastIndexOf("?,"),sb.length(),"? ");
        sb.append("where id=?");
		return sb.toString();
	}
	/**
	 * 查询语句的sql
	 * @param clazz
	 * @param condition
	 * @return
	 */
	public static String getSelectSql(Class clazz,String condition) {
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		//获取表名
		String tableName=TableUtil.getTableNameByClass(clazz);
		sb.append(tableName+" ");
		//字段填充
		sb.append(condition);
		return sb.toString();
	}
	
	
	
	
	
	
	

}