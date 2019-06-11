package com.cqeec.util.core;

import java.lang.reflect.Field;

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
	
	
}
