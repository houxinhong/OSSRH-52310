package com.cqeec.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.cqeec.annotation.Column;
import com.cqeec.annotation.Id;

public class ColumnUtil {

	public static String getColumnNameByField(Field field) {
		Id id=field.getDeclaredAnnotation(Id.class);
		if(id!=null) {
			return id.value(); 
		}
		
		Column column=field.getAnnotation(Column.class);
		if(column==null&&id==null) {
			String temp=field.getName();
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
			String columnName=column.value();
			return columnName;
		}
	};
	
	
	public static String getFieldName(Class clazz,String ColumnName){
		Field[] fields=clazz.getDeclaredFields();
		for(Field field:fields) {
			Id id=field.getDeclaredAnnotation(Id.class);
			Column column=field.getDeclaredAnnotation(Column.class);
			if(id!=null) {
					if(id.value().equals(ColumnName)) {
						return field.getName();
					}
			}
			if(column!=null) {
				if(column.value().equals(ColumnName)) {
					return field.getName();
				}
			}
			
		}
		try {
			return clazz.getDeclaredField(ColumnName).getName();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	};
	
	public static String getColumnName(Class clazz,String fieldName){
		Id id=null;
		Column column=null;
		try {
			id = clazz.getField(fieldName).getDeclaredAnnotation(Id.class);
			 column=clazz.getField(fieldName).getDeclaredAnnotation(Column.class);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		if(id!=null)return id.value();
		if(column!=null)return column.value();
		return fieldName;
	};
	
	public static Object callPKGetMethod(Object obj) {
		Class clazz=obj.getClass();
		String keyFieldName=ClassUtil.getPrimaryKeyFieldName(clazz);
		String keyGetMethodName="get"+ClassUtil.getClassSimpleName(keyFieldName);
		try {
			Method method=clazz.getMethod(keyGetMethodName);
			return method.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
