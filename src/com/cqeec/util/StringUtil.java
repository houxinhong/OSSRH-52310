package com.cqeec.util;
public class StringUtil {
	
	
	
	public static String firstLetterUpper(String str) {
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	public static String firstLetterLower(String str) {
		return str.substring(0, 1).toLowerCase()+str.substring(1);
	}
	public static Integer  getFirstUpperLetterIndex(String str) {
		for(int i=0;i<str.length();i++) {
			if(Character.isUpperCase(str.charAt(i))) {
				return i;
			}
		}
		return null;
	}
	
	
	
	
	
}
