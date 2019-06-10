package com.cqeec.test;

import org.junit.Test;

import com.cqeec.util.ClassUtil;

public class getClassesTest {

	@Test
	public void test01() {
		 Object[] ts =   ClassUtil.getClasses("com.cqeec").toArray();
	        for(Object t:ts){
	            Class<?> tt = (Class<?>) t;
	            System.out.println(tt.getName());
	        }
	}
	
	
}
