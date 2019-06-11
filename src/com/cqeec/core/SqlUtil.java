package com.cqeec.core;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.cqeec.bean.ColumnInfo;
import com.cqeec.bean.TableInfo;
import com.cqeec.util.core.ClassUtil;
import com.cqeec.util.core.ColumnUtil;
import com.cqeec.util.core.FieldUtil;
import com.cqeec.util.core.TableUtil;
import com.cqeec.util.other.StringUtil;
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
        	if(!ColumnUtil.getColumnNameByField(field).equals(ClassUtil.getPrimaryKeyByClass(clazz))) {
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
		if("true".equals(GlobalParams.getProperties().get("isShowSql"))) {
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

	public static Integer save(String sql, Object pojo) {
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
	        
	            try {
	            	 boolean flag=FieldUtil.IsSelectPK(pojo.getClass());
	     	        if(flag) {
	     	        	ResultSet rs=DBUtil.getConn().prepareStatement("SELECT @@IDENTITY").executeQuery();
	     	        	rs.next();
	     	        	Integer id=rs.getInt(1);
	     	        	return id;
	     	        }
				} catch (Exception e) {
					new RuntimeException("查询插入自增主键错误");
				}
	        return null;
	}

	public static void delete(String sql, Object id) {
           modify(sql, id);		
	}

	public static List<Object> select(String sql,Class clazz,Object... params) {
		if(GlobalParams.getProperties().get("isShowSql").equals("true")) {
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
	 * 行映射器
	 * 将mysql数据转换为java对象
	 * @param rs
	 * @param clazz
	 * @return
	 */
	private static Object mysqlData2Java(ResultSet rs,Class clazz) {
		Object obj=null;
	try {
		obj=clazz.newInstance();
		ResultSetMetaData metaData=rs.getMetaData();
		int columnCount=metaData.getColumnCount();
		for(int i=1;i<=columnCount;i++) {
			String columnName=metaData.getColumnName(i);
			String columnType=metaData.getColumnTypeName(i);
			Class paramType=MySqlTypeConvertor.databaseType2JavaType(columnType);
		   Field field=FieldUtil.getFieldByColumnName(clazz, columnName);
			//获取字段名称(有注解就用注解上的)
			String columnNameAnno=ColumnUtil.getColumnName(clazz, field.getName());
			if(columnName.equals(columnNameAnno)) {
				//根据字段名称获取到相应的set方法
				Method method=clazz.getDeclaredMethod("set"+ClassUtil.getClassSimpleName(field.getName()), paramType);
				method.invoke(obj, rs.getObject(columnName));
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	 }
	return obj;
	}
	//==========================================
	//之前的mysqlData2Java方法代码冗余，更新之后是上面的样子//
	//==========================================
	/*
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
			String property2MethodName="set"+ClassUtil.getClassSimpleName(StringUtil.firstLetterUpper(field.getName()));
			
			for(Method method:methods) {
				if(method.getName().equals(property2MethodName)) {
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
*/	
	
	
	

}