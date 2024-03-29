package com.cqeec.util.other;

import java.util.Map;

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
	
	
	public static String parseCache(Map<Integer, String> cache) {
		StringBuffer sb=new StringBuffer();
       for(int i=1;i<=10;i++) {
    	   if(cache.containsKey(i)) {
    		   sb.append(cache.get(i));
    	   }
       }
		return sb.toString();
	}
	/**
	 * 截取字符串缓存最后四位，判断其中是否有OR
	 * @param sb
	 * @return
	 */
	public static boolean isLastKeywordEqualToOR(StringBuffer sb){
          int length=sb.length();
          return sb.subSequence(length-4, length).toString().contains("OR");          
	}
	
	
	
	
	
	
}
