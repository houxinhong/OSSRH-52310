package com.cqeec.util.core;

import java.lang.reflect.Field;

import com.cqeec.annotation.Id;

public class FieldUtil {

	/**
	 * 根据表字段名称与Class类找到Field
	 */
	
	public static Field getFieldByColumnName(Class clazz,String columnName) {
			Field field;
			try {
				field = clazz.getDeclaredField(columnName);
			} catch (NoSuchFieldException | SecurityException e) {
				field=null;
			}
		//如果类属性没有带字段映射注解则field不为null
		if(field!=null) {
			return field;
		}else {
			//如果类属性有字段映射注解则field为null
			for(Field temp:clazz.getFields()) {
				String columnNameAnno=ColumnUtil.getColumnNameByField(temp);
				if(columnName.equals(columnNameAnno)) {
					return temp;
				}
			}
		}
		throw new RuntimeException("找不到与表字段对应的类属性");
		
		
		
		
	}

	public static boolean IsSelectPK(Class<? extends Object> class1) {
		 String fieldName=ClassUtil.getPrimaryKeyFieldName(class1);
		 Field  pk=null;
		 try {
			pk=class1.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException("没有找到主键异常");
		}
		 Id id=pk.getDeclaredAnnotation(Id.class);
		 boolean flag=id.auto_increment();
		 return flag;
	}
	
	
}
