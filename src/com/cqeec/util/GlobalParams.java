package com.cqeec.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cqeec.bean.TableInfo;
import com.squareup.javapoet.ClassName;

public class GlobalParams {
	//全部表信息
   public static List<TableInfo> tableInfos=TableUtil.getTables(); 
   //
   public static Map<ClassName, TableInfo> ClassName2TableMap=new HashMap<>();
   //全部表信息的Map集合key-->Class Value-->TableInfo	
    public static Map<Class, TableInfo> tableInfosMap=TableUtil.getTableInfoMap();
   
}
