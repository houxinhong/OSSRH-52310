package com.cqeec.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cqeec.bean.TableInfo;
import com.cqeec.util.core.ClassUtil;
import com.cqeec.util.other.FileParseUtil;
import com.squareup.javapoet.ClassName;

/**
 * 懒汉设计模式彻底解决配置文件不能随意配置名称与路径问题！！！！！！！！！！！！！！！！！！！！！
 * @author Administrator
 *
 */
public class GlobalParams {
	private static String path="config.properties";
   //全部表信息的Map集合key-->ClassName Value-->TableInfo	
   private static Map<ClassName, TableInfo> ClassName2TableMap;
   //配置文件
   private static Properties properties; 
   @Deprecated
   //全部表信息  --->没用到
   private static List<TableInfo> tableInfos; //TableUtil.getTables()
   @Deprecated
   //全部表信息的Map集合key-->Class Value-->TableInfo	 需要java类才能获取class对象，这里就不一开始初始化
   //改进代码后用不到这里了
   private static Map<Class, TableInfo> tableInfosMap; //TableUtil.getTableInfoMap()
   
   
   
	
	public static void setPath(String path) {
	   GlobalParams.path = path;
    }
	public static Map<ClassName, TableInfo> getClassName2TableMap() {
		return ClassName2TableMap==null?ClassUtil.getClassName_tableInfoMap(getProperties()):ClassName2TableMap;
	}
	public static Properties getProperties() {
		return properties==null?FileParseUtil.parsePropertyFile(path):properties;
	}
	
   
}
