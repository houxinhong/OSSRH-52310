package com.cqeec.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class CollectionUtil {
	
	public static List<Object> packingParams(Object... objects){
		List<Object> list=new ArrayList<>();
		for(Object object:objects) {
			list.add(object);
		}
		return list;
	}

	public static Object[] sortByUpdate(Object object) {
		Field[] fields=object.getClass().getDeclaredFields(); 
		Method[] methods=object.getClass().getDeclaredMethods();
		Object[] params=new Object[fields.length];		
		int index=0;
		for(Field field:fields) {
			if(!field.getName().equals("id")) {
				for(Method method:methods) {
					if(method.getName().contains("get"+StringUtil.firstLetterUpper(field.getName()))) {
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
			params[index]=object.getClass().getDeclaredMethod("getId").invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

}
