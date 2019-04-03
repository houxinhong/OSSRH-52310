package com.cqeec.util;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.cqeec.bean.ColumnInfo;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.MySqlTypeConvertor;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.MethodSpec.Builder;

public class GenerateCodeUtil {
	/**
	 * 生成java文件
	 */
	public static void generateJavaFile(){
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
				MethodSpec constructor=methodBuilder.build();
				
				//将属性与方法添加进类中
				String simpleClassName=ClassUtil.getClassSimpleName(tableInfo.getTname());
				TypeSpec type=TypeSpec.classBuilder(simpleClassName)
				.addMethods(methods)
				.addMethod(constructor)
				.addMethod(MethodSpec.constructorBuilder().build())
				.addFields(fields)
				.addModifiers(Modifier.PUBLIC).build();
				//生成对应的java文件
				String targetPackage=FileParseUtil.parsePropertyFile("config.properties").getProperty("targetPackage");
				String targetProject=FileParseUtil.parsePropertyFile("config.properties").getProperty("targetProject");
				JavaFile javaFile=JavaFile.builder(targetPackage, type).build();
				//将java类输出到指定目录 
				try {
					javaFile.writeTo(new File(targetProject));
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		  }
		  catch (SQLException e) {
		      e.printStackTrace();
	      }
	}
	
	/**
	 * 生成映射器
	 */
	public static void generateMapper() {
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
