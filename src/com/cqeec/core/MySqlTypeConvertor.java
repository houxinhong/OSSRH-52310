package com.cqeec.core;
/**
 * mysql数据类型和java数据类型的转换
 * @author gaoqi
 *
 */
public class MySqlTypeConvertor {

	public static Class databaseType2JavaType(String columnType) {
		//varchar-->String
		if("varchar".equalsIgnoreCase(columnType)||"char".equalsIgnoreCase(columnType)||"text".equalsIgnoreCase(columnType)){
			return String.class;
		}
		//int smallint integer-->Integer
	    if("int".equalsIgnoreCase(columnType)||"smallint".equalsIgnoreCase(columnType)||"integer".equalsIgnoreCase(columnType)){
			return Integer.class;
		}
	    //tinyint bit-->Boolean
	    if("tinyint".equalsIgnoreCase(columnType)||"bit".equalsIgnoreCase(columnType)) {
	    	return Boolean.class;
	    }
		//bigint-->Long
		if("bigint".equalsIgnoreCase(columnType)){
			return Long.class;
		}
		//double-->Double
		if("double".equalsIgnoreCase(columnType)||"float".equalsIgnoreCase(columnType)){
			return Double.class;
		}
		return null;
	}

	public static String javaType2DatabaseType(String javaDataType) {
		return null;
	}

}