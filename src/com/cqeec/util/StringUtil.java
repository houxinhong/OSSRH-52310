package com.cqeec.util;
public class StringUtil {
	
	
	
	public static String firstLetterUpper(String str) {
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	public static String firstLetterLower(String str) {
		return str.substring(0, 1).toLowerCase()+str.substring(1);
	}
	/**
	 * 获取第一个大写字母的下标
	 * @param str
	 * @return
	 */
	public static Integer  getFirstUpperLetterIndex(String str) {
		for(int i=0;i<str.length();i++) {
			if(Character.isUpperCase(str.charAt(i))) {
				return i;
			}
		}
		return null;
	}
	/**
	 * 清除字符串缓存对象的末尾字符
	 * @param sb
	 */
	public static void clearEndChar(StringBuffer sb) {
		sb.replace(sb.length()-1,sb.length(),"");
	}
	
	
	public static String spot2Slash(String str) {
		return str.replace('.', '\\');
	}
	
	
	
	
	
	
	
}
