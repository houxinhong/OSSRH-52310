package com.cqeec.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cqeec.annotation.Id;
import com.cqeec.annotation.Table;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.MySqlTypeConvertor;
import com.squareup.javapoet.ClassName;



public class ClassUtil {
	
	/**
	 * 根据Class对象获取简单类名
	 * （如com.cqeec.pojo.User--->User）
	 * @param clazz
	 * @return
	 */
	public static String getClassSimpleName(Class clazz) {
		String qualifiedName=clazz.getName();
		String[] names=qualifiedName.split("\\.");
		return names[names.length-1];
	}
	/**
	 * 根据表名生成简单类名
	 * @param tableName
	 * @return
	 */
	public static String getClassSimpleName(String tableName) {
		if(tableName.indexOf("_")!=-1) {
			String[] temps=tableName.split("_");
			StringBuffer sb=new StringBuffer();
			for(String temp:temps) {
				sb.append(StringUtil.firstLetterUpper(temp));
			}
			tableName=sb.toString();
		}else {
			tableName=StringUtil.firstLetterUpper(tableName);
		}
		return tableName;
	}
	
	
	/**
	 * 调用obj对象对应属性fieldName的get方法
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public static Object invokeGet(String fieldName,Object obj){
		try {
			Class c = obj.getClass();
			Method m = c.getDeclaredMethod("get"+StringUtil.firstLetterUpper(fieldName), null);
			return m.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * 调用set方法
	 * @param obj
	 * @param columnName
	 * @param columnValue
	 */
	public static void invokeSet(Object obj,String columnName,Object columnValue){
		try {
			Method m = obj.getClass().getDeclaredMethod("set"+StringUtil.firstLetterUpper(columnName), 
					columnValue.getClass());
			m.invoke(obj, columnValue);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	public static String getQuilifiedName(String tname) {
		String simple=getClassSimpleName(tname);
		String  quilified=FileParseUtil.parsePropertyFile("config.properties").getProperty("targetPackage")+"."+simple;
		return quilified;
	}
	/**
	 * 获取指定包目录下的Class集合
	 * @param targetPackage
	 * @return
	 */
	public static List<Class> getClassListByPackage(String path) {
		// TODO Auto-generated method stub
		List<Class> list=new ArrayList<>();
		Properties prop=GlobalParams.properties;
		File directory=new File(path);
		File[] files=directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.contains(".java")&&!name.contains("Mapper")) {
					return true;
				}
				return false;
			}
		});
		for(File file:files) {
			String simpleName=file.getName().substring(0,file.getName().indexOf(".java"));
			String qualifiedName=prop.get("targetPackage")+"."+simpleName;
			try {
				list.add(Class.forName(qualifiedName));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}
	public static List<ClassName> getClassNameList(Properties prop) {
		
		List<TableInfo> list=TableUtil.getTables();
		List<ClassName> list2=new ArrayList<>();
		for(TableInfo tableInfo:list) {
		    ClassName className=ClassName.get(prop.getProperty("targetPackage"),ClassUtil.getClassSimpleName(tableInfo.getTname())); 
			list2.add(className);
			GlobalParams.ClassName2TableMap.put(className,tableInfo);
		}
		return list2;
	}
	
	public static Map<ClassName, TableInfo> getClassName_tableInfoMap(Properties prop){
		List<TableInfo> list=TableUtil.getTables();
		List<ClassName> list2=new ArrayList<>();
		Map<ClassName, TableInfo> map=new HashMap<>();
		for(TableInfo tableInfo:list) {
		    ClassName className=ClassName.get(prop.getProperty("targetPackage"),ClassUtil.getClassSimpleName(tableInfo.getTname())); 
			list2.add(className);
			map.put(className,tableInfo);
		}
		return map;
	}
	
	
	
	public static Class getTypeByFieldNameAndClassName(String string, ClassName clazz) {
		String columnType=GlobalParams.ClassName2TableMap.get(clazz).getColumns().get(string).getDataType();
		return MySqlTypeConvertor.databaseType2JavaType(columnType);
	}
	
	
	public static Class getArrayClassByClass(Class clazz) {
		//-------------------
		if(clazz.equals(String.class)) {
			return String[].class;
		}
		if(clazz.equals(Integer.class)) {
			return Integer[].class;
			
		}
		if(clazz.equals(Boolean.class)) {
			return Boolean[].class;
			
		}
		if(clazz.equals(Long.class)) {
			return Long[].class;
			
		}
		if(clazz.equals(Double.class)) {
			return Double[].class;
			
		}
		return null;
	}
	/**
	 * 由于加入类名与表名对应的注解所以这个方法应谨慎使用
	 * @param clazz
	 * @return
	 */
	public static String getPrimaryKeyByClassName(ClassName clazz) {
		return GlobalParams.ClassName2TableMap.get(clazz).getOnlyPriKey().getName();
	}
	
	public static String getPrimaryKeyByClass(Class clazz) {
		Method[] methods=clazz.getDeclaredMethods();
		for(Method method:methods) {
			Id id=method.getDeclaredAnnotation(Id.class);
			if(id!=null) {
				return id.value(); 
			}
		}
		
		Table table=(Table) clazz.getDeclaredAnnotation(Table.class);
		if(table==null) {
			return getPrimaryKeyByClassName(ClassName.get(clazz));
		}else {
			 String tableName=table.value();
			 String simpleName=getClassSimpleName(tableName);
			return getPrimaryKeyByClassName(ClassName.get(GlobalParams.properties.getProperty("targetPackage"), simpleName));
		}
	}
}
