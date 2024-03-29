package com.cqeec.util.other;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.cqeec.util.core.ClassUtil;
import com.cqeec.util.core.ColumnUtil;
import com.squareup.javapoet.ClassName;


public class CollectionUtil {
	
	public static List<Object> packingParams(Object... objects){
		List<Object> list=new ArrayList<>();
		for(Object object:objects) {
			list.add(object);
		}
		return list;
	}
/**
 * 这里的的顺序是基于Class获取的filed数组的遍历顺序
 * @param object
 * @return
 */
	public static Object[] sortByUpdate(Object object) {
		Field[] fields=object.getClass().getDeclaredFields(); 
		Method[] methods=object.getClass().getDeclaredMethods();
		Object[] params=new Object[fields.length];		
		int index=0;
		for(Field field:fields) {
			if(!ColumnUtil.getColumnNameByField(field).equals(ClassUtil.getPrimaryKeyByClass(object.getClass()))) {
				for(Method method:methods) {
					if(method.getName().contains("get"+ClassUtil.getClassSimpleName(field.getName()))) {
						try {
								params[index]=method.invoke(object);
						} catch (Exception e) {
							// work Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				index++;		
			}
		}
		try {
			params[index]=object.getClass().getDeclaredMethod("get"+ClassUtil.getClassSimpleName(ClassUtil.getPrimaryKeyFieldName(object.getClass()))).invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

}
