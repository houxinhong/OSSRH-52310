package com.cqeec.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.cqeec.bean.ColumnInfo;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.MySqlTypeConvertor;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;



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
}
