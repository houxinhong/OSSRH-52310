package com.cqeec.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cqeec.bean.TableInfo;
import com.squareup.javapoet.ClassName;

public class GlobalParams {
	static{
		properties=FileParseUtil.parsePropertyFile("config.properties");
		tableInfos=TableUtil.getTables();
		ClassName2TableMap=new HashMap<>();
	}
	//全部表信息
   public static List<TableInfo> tableInfos; 
   //
   public static Map<ClassName, TableInfo> ClassName2TableMap;
   //全部表信息的Map集合key-->Class Value-->TableInfo	 需要java类才能获取class对象，这里就不一开始初始化
   public static Map<Class, TableInfo> tableInfosMap;
   //配置文件
   public static Properties properties; 
   
}
