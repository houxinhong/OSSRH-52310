package com.cqeec.util;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.cqeec.bean.ColumnInfo;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.MySqlTypeConvertor;
import com.cqeec.pojo.Role;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.MethodSpec.Builder;

public class GenerateCodeUtil {
	/**
	 * 生成java文件
	 */
	public static void generateJavaFile(String configPath){
		//获取所有表信息
		try {
			List<TableInfo> list=TableUtil.getTables();
			//便利表信息
			for(TableInfo tableInfo:list) {
				//属性集合与方法集合
				List<FieldSpec> fields=new ArrayList<>();
				List<MethodSpec> methods=new ArrayList<>();
				Builder methodBuilder=MethodSpec.constructorBuilder();
				
				Map<String, ColumnInfo> map=tableInfo.getColumns();
				Set<String> keySet=map.keySet();
				
				//创建对应的字段
				//创建相应的get与set方法
				for(String key:keySet) {
					//字段
					ColumnInfo columnInfo=map.get(key);
					String databaseFieldType=columnInfo.getDataType();
					Class type_=MySqlTypeConvertor.databaseType2JavaType(databaseFieldType);
					FieldSpec field=FieldSpec.builder(type_, key, Modifier.PRIVATE).build();
					fields.add(field);
					//方法
					String name=StringUtil.firstLetterUpper(key);
					MethodSpec get=MethodSpec.methodBuilder("get"+name).
							addModifiers(Modifier.PUBLIC).returns(type_).
							addStatement("return $L",key).build();
					MethodSpec set=MethodSpec.methodBuilder("set"+name).
							addParameter(type_, key).
							addModifiers(Modifier.PUBLIC).
							returns(void.class).addStatement("this.$L=$L",key,key).build();
					methodBuilder.addParameter(type_, key);
					methodBuilder.addStatement("this.$L=$L",key,key);
					methods.add(get);
					methods.add(set);
				}
				methodBuilder.addModifiers(Modifier.PUBLIC);
				MethodSpec constructor=methodBuilder.build();
				
				//将属性与方法添加进类中
				String simpleClassName=ClassUtil.getClassSimpleName(tableInfo.getTname());
				TypeSpec type=TypeSpec.classBuilder(simpleClassName)
				.addMethods(methods)
				.addMethod(constructor)
				.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build())
				.addFields(fields)
				.addModifiers(Modifier.PUBLIC).build();
				//生成对应的java文件
				String targetPackage=FileParseUtil.parsePropertyFile(configPath).getProperty("targetPackage");
				String targetProject=FileParseUtil.parsePropertyFile(configPath).getProperty("targetProject");
				JavaFile javaFile=JavaFile.builder(targetPackage, type).build();
				//将java类输出到指定目录 
				try {
					javaFile.writeTo(new File(targetProject));
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		  }
		  catch (Exception e) {
		      e.printStackTrace();
	      }
	}
	
	/**
	 * 生成映射器
	 */
	public static void generateMapper() {
		  Properties prop=FileParseUtil.parsePropertyFile("config.properties");
		  String targetProject=prop.getProperty("targetProject");
		  String targetPackage=prop.getProperty("targetPackage");
		  //获取指定包下所有的Class
		  List<Class> list=ClassUtil.getClassListByPackage(targetProject+"\\"+StringUtil.spot2Slash(targetPackage));
		  
		 //生成相应java文件
		  for(Class clazz:list) {
			  String firstLowerClassName=StringUtil.firstLetterUpper(ClassUtil.getClassSimpleName(clazz));
			  String className=ClassUtil.getClassSimpleName(clazz);
			//基本的增删改方法
	          MethodSpec insert=MethodSpec.methodBuilder("insert").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(clazz,firstLowerClassName).
	        		  addStatement("String sql=$T.getInsertSql($L.getClass())",SqlUtil.class,firstLowerClassName).
	        		  addStatement("$T.save(sql,$L)",SqlUtil.class,firstLowerClassName).
	        		  build();
	          MethodSpec delete=MethodSpec.methodBuilder("delete").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(long.class,"id").
	        		  addStatement("String sql=$T.getDeleteSql($L.class)+\"where id=?\"",SqlUtil.class,className).
	        		  addStatement("$T.delete(sql,id)",SqlUtil.class).
	        		  build();
	          MethodSpec update=MethodSpec.methodBuilder("update").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(clazz,firstLowerClassName).
	        		  addStatement("String sql=$T.getUpdateSql($L.getClass())",SqlUtil.class,className).
	        		  addStatement("$T.modify(sql,$T.sortByUpdate($L))",SqlUtil.class,CollectionUtil.class,className).
	        		  build();
	          MethodSpec select=MethodSpec.methodBuilder("select").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(clazz).
	        		  addParameter(long.class,"id").
	        		  addStatement("String sql=$T.getSelectSql($L.class, \"where id=?\")",SqlUtil.class,className).
	        		  addStatement("return SqlUtil.select(sql,$L.class,id)!=null?($L)SqlUtil.select(sql,$L.class,id).get(0):null",className,className,className).
	        		  build();
		  }
		  
		  
		  
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
