package com.cqeec.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.cqeec.bean.ColumnInfo;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.MySqlTypeConvertor;
import com.squareup.javapoet.ClassName;


public class SqlUtil {
	/**
	 * 这里的的顺序是基于Class获取的filed数组的遍历顺序
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
		//设置字段名
		Field[] fields=clazz.getDeclaredFields();
		sb.append("(");
		for(Field field:fields) {
			sb.append(ColumnUtil.getColumnNameByField(field)+",");
		}
		StringUtil.clearEndChar(sb);
		sb.append(")");
		//设置问号数量
		sb.append("values(");
        for(Field field:fields) {
        	sb.append("?,");
        }
		StringUtil.clearEndChar(sb);
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
	 * 这里的的顺序是基于Class获取的filed数组的遍历顺序
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
		sb.append("set ");
        for(Field field:fields) {
        	if(!field.getName().equals(ClassUtil.getPrimaryKeyByClassName(ClassName.get(clazz)))) {
            	sb.append(ColumnUtil.getColumnNameByField(field));
            	sb.append("=");
            	sb.append("?,");
        	}
        }
        sb.replace(sb.lastIndexOf("?,"),sb.length(),"? ");
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
		if(condition==null) {
			return sb.toString();
		}else {
			return sb.toString()+condition;
		}
	}
	
	
	public static void modify(String sql,Object... params) {
		if(GlobalParams.properties.get("isShowSql").equals("true")) {
			System.out.println(sql);
		}
		try {
			Connection con=DBUtil.getConn();
			PreparedStatement ps=con.prepareStatement(sql);
			int sort=1;
			for(Object param:params) {
				 ps.setObject(sort, param);
				 sort++;
			}
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save(String sql, Object pojo) {
		 Field[] fields=pojo.getClass().getDeclaredFields(); 
	        Object[] params=new Object[fields.length];
	        Method[] methods=pojo.getClass().getDeclaredMethods();
	        int index=0;
	        for(Field field:fields) {
	        	String property2MethodName=ClassUtil.getClassSimpleName(StringUtil.firstLetterUpper(field.getName()));
	        	for(Method method:methods) {
	        		if(method.getName().equals("get"+property2MethodName)) {
	        			try {
							params[index]=method.invoke(pojo);
						} catch (Exception e) {
							e.printStackTrace();
						}
	        		}
	        	}
	        	index++;
	        }
	        modify(sql, params);
	}

	public static void delete(String sql, Object id) {
           modify(sql, id);		
	}

	public static List<Object> select(String sql,Class clazz,Object... params) {
		if(GlobalParams.properties.get("isShowSql").equals("true")) {
			System.out.println(sql);
		}
		try {
			List<Object> list=new ArrayList<>();
			Connection con=DBUtil.getConn();
			PreparedStatement ps=con.prepareStatement(sql);
			int index=1;
			if(params!=null) {
				for(Object param:params) {
					ps.setObject(index,param);
					index++;
				}
			}
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				Object object=mysqlData2Java(rs, clazz);
				list.add(object);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将mysql数据转换为java对象
	 * @param rs
	 * @param clazz
	 * @return
	 */
	private static Object mysqlData2Java(ResultSet rs,Class clazz) {
		Field[] fields=clazz.getDeclaredFields();
		Method[] methods=clazz.getDeclaredMethods();
		if(GlobalParams.tableInfosMap==null) {
			GlobalParams.tableInfosMap=TableUtil.getTableInfoMap();
		}
		TableInfo tableInfo=GlobalParams.tableInfosMap.get(clazz);
		Map<String, ColumnInfo> columns=tableInfo.getColumns();
		Object obj=null;
	try {
		obj=clazz.newInstance();
		for(Field field:fields) {
			String dataType=columns.get(ColumnUtil.getColumnNameByField(field)).getDataType();
			Class  javaType=MySqlTypeConvertor.databaseType2JavaType(dataType);
			//属性对应的方法名称
			String property2MethodName=ClassUtil.getClassSimpleName(StringUtil.firstLetterUpper(field.getName()));
			
			for(Method method:methods) {
				if(method.getName().equals("set"+property2MethodName)) {
					//数据库中字段名称
					String columnName=ColumnUtil.getColumnNameByField(field);
					if(javaType.equals(String.class)) {
						method.invoke(obj, rs.getString(columnName));
					}
					if(javaType.equals(Integer.class)) {
						method.invoke(obj, rs.getInt(columnName));
					}
					if(javaType.equals(Boolean.class)) {
						method.invoke(obj, rs.getBoolean(columnName));
					}
					if(javaType.equals(Double.class)) {
						method.invoke(obj, rs.getDouble(columnName));
					}
					if(javaType.equals(Long.class)) {
						method.invoke(obj, rs.getLong(columnName));
					}
				}
			}
			
		}
	} catch (Exception e) {
		e.printStackTrace();
	 }
	return obj;
	}
	
	
	
	
	
	

}