package com.cqeec.util.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.cqeec.annotation.Column;
import com.cqeec.annotation.Table;
import com.cqeec.bean.ColumnInfo;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.DBUtil;
import com.cqeec.core.GlobalParams;
import com.cqeec.util.other.StringUtil;

public class TableUtil {
	
	public static List<TableInfo> getTables() {
		List<TableInfo> list=new ArrayList<>();
		try {
			//初始化获得表的信息
			Connection con = DBUtil.getConn();
			//获取数据库信息
			DatabaseMetaData dbmd = con.getMetaData(); 
			//获取表集合
			ResultSet tableRet = dbmd.getTables(null, "%","%",new String[]{"TABLE"}); 
			//便利表集合
			while(tableRet.next()){
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(),new HashMap<String,ColumnInfo>());
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");  //查询表中的所有字段
				while(set.next()){
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), 
							set.getString("TYPE_NAME"), 0);
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}
				
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);  //查询t_user表中的主键
				while(set2.next()){
					ColumnInfo ci2 = (ColumnInfo) ti.getColumns().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);  //设置为主键类型
					ti.getPriKeys().add(ci2);
				}
				
				if(ti.getPriKeys().size()>0){  //取唯一主键。。方便使用。如果是联合主键。则为空！
					ti.setOnlyPriKey(ti.getPriKeys().get(0));
				}
				//添加表数据
				list.add(ti);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	@Deprecated
	public static Map<Class ,TableInfo > getTableInfoMap() {
	 //这种写法无法进行表与类之间通过注解的形式进行映射，会调用Classfrom()方法，去加载一个可能不存在的Class
		
     /*Map<Class, TableInfo> map=new HashMap<>();
	 List<TableInfo> list=getTables();
	 for(TableInfo tableInfo:list) {
		Properties properties=GlobalParams.properties;
		Class clazz=ClassUtil.getClassByTableInfo(tableInfo);
		map.put(clazz, tableInfo);
	 }*/
		
	 Map<Class, TableInfo> map=new HashMap<>();
	 Set<Class<?>> set=ClassUtil.getClasses(GlobalParams.getProperties().getProperty("targetPackage"));
	 List<TableInfo> list=getTables();
	 for(TableInfo tableInfo:list) {
		for(Class<?> clazz:set) {
			String tableName=getTableNameByClass(clazz);
			if(tableInfo.getTname().equals(tableName)) {
				map.put(clazz, tableInfo);
			}
		}
	 }
	return map;
		
	}
	/**
	 * 根据Class对象获取表名
	 * @param clazz
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static String getTableNameByClass(Class clazz) {
		Table table=(Table)clazz.getAnnotation(Table.class);
		if(table==null) {
			String simpleClassName=ClassUtil.getClassSimpleName(clazz);
			//parseTableName
			String temp=StringUtil.firstLetterLower(simpleClassName);
			//当类名有大写字母时才做操作
			if(StringUtil.getFirstUpperLetterIndex(temp)!=null) {
				Integer index=StringUtil.getFirstUpperLetterIndex(temp);
				while(index!=null) {
					String first=temp.substring(0,index);
					String second=temp.substring(index);
					second="_"+StringUtil.firstLetterLower(second);
					temp=first+second;
					index=StringUtil.getFirstUpperLetterIndex(temp);
				}
			}
			return temp;
		}else {
			return table.value();
		}
		
	};
}