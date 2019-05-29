package com.cqeec.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.lang.model.element.Modifier;

import com.cqeec.bean.ColumnInfo;
import com.cqeec.bean.PageInfo;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.MySqlTypeConvertor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;

public class GenerateCodeUtil {
	public static void main(String[] args) {
		generateJavaFile("config.properties");
		generateMapper("config.properties");
	}
	
	
	/**
	 * 生成映射器
	 */
	public static void generateMapper(String path) {
		  //之前获取class会去包中去找相应的java文件（所以执行该方法之前会必须有java实体类）
		  //为了解耦----这里这样是改变了---通过拼接字符串得到相应的ClassName对象
		  Properties prop=GlobalParams.properties;
		  List<ClassName> list=ClassUtil.getClassNameList(prop);
		  //如果没有的话则生存对应的包
		  File directory=new File(prop.getProperty("targetProject")+"\\"+StringUtil.spot2Slash(prop.getProperty("targetPackage")));
		  if(!directory.exists()) {
			  directory.mkdirs();
		  }
		  
		 //生成相应java文件
		  for(ClassName clazz:list) {
			  //根据ClassName获取相应表的主键-----只支持单主键
			  String primaryKey__=ClassUtil.getPrimaryKeyByClassName(clazz);
			  //获取当前便利ClassName对应的实体类的id类型
			  Class primaryKey_class=ClassUtil.getTypeByFieldNameAndClassName(primaryKey__,clazz);
			  
			  //成员方法集合
			  List<MethodSpec> methodSpecs=new ArrayList<>();
			  
			  String firstLowerClassName=StringUtil.firstLetterLower(clazz.simpleName());
			  String className=clazz.simpleName();
			  //
			  com.squareup.javapoet.TypeSpec.Builder typeTemp=TypeSpec.classBuilder(className+"Mapper").
					  addModifiers(Modifier.PUBLIC);
			  
			//添加开启事务与提交事务的静态方法  
			  MethodSpec enabletransaction=MethodSpec.methodBuilder("enabletransaction").
					  addModifiers(Modifier.PUBLIC,Modifier.STATIC).
					  returns(void.class).
					  addStatement("$T.enableTransaction()",DBUtil.class).
					  build();
			  methodSpecs.add(enabletransaction);
			  
			  MethodSpec commit=MethodSpec.methodBuilder("commit").
					  addModifiers(Modifier.PUBLIC,Modifier.STATIC).
					  returns(void.class).
					  addStatement("$T.commitTransaction()",DBUtil.class).
					  build();
			  methodSpecs.add(commit);
			  
			//work 基本的增删改方法
			  
	          MethodSpec insert=MethodSpec.methodBuilder("insert").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(clazz,firstLowerClassName).
	        		  addStatement("String sql=$T.getInsertSql($L.getClass())",SqlUtil.class,firstLowerClassName).
	        		  addStatement("$T.save(sql,$L)",SqlUtil.class,firstLowerClassName).
	        		  build();
	          methodSpecs.add(insert);
	          MethodSpec delete=MethodSpec.methodBuilder("delete").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(primaryKey_class,"primaryKey").
	        		  addStatement("String sql=$T.getDeleteSql($L.class)+\"where \"+ClassUtil.getPrimaryKeyByClass($L.class)+\"=?\"",SqlUtil.class,className,className).
	        		  addStatement("$T.delete(sql,primaryKey)",SqlUtil.class).
	        		  build();
	          methodSpecs.add(delete);
	          MethodSpec update=MethodSpec.methodBuilder("update").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(clazz,firstLowerClassName).
	        		  addStatement("String sql=$T.getUpdateSql($L.getClass())+\"where \"+ClassUtil.getPrimaryKeyByClass($L.class)+\"=?\"",SqlUtil.class,firstLowerClassName,className).
	        		  addStatement("$T.modify(sql,$T.sortByUpdate($L))",SqlUtil.class,CollectionUtil.class,firstLowerClassName).
	        		  build();
	          methodSpecs.add(update);
	          MethodSpec select=MethodSpec.methodBuilder("select").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(clazz).
	        		  addParameter(primaryKey_class,"primaryKey").
	        		  addStatement("String sql=$T.getSelectSql($L.class, \"where \"+$T.getPrimaryKeyByClass($L.class)+\"=?\")",SqlUtil.class,className,ClassUtil.class,className).
	        		  addStatement("List<Object> temp=SqlUtil.select(sql,$L.class,primaryKey)",className).
	        		  addStatement("return temp!=null&&temp.size()!=0?($L)temp.get(0):null",className).
	        		  build();
	          methodSpecs.add(select);
	          //work 批量增删改查
	          ClassName list_=ClassName.get(List.class);
	          ClassName object_=ClassName.get(Object.class);
	          TypeName TypeNameListChangeObj=ParameterizedTypeName.get(list_, clazz);
	          TypeName classNameListObject=ParameterizedTypeName.get(list_,object_);
	          
	          MethodSpec batchInsert=MethodSpec.methodBuilder("batchInsert").
	        		     addModifiers(Modifier.PUBLIC).
	        		     returns(void.class).
	        		     addParameter(TypeNameListChangeObj, firstLowerClassName+"s").
	        		     beginControlFlow("for($T $L:$L)", clazz,firstLowerClassName,firstLowerClassName+"s").
	        		     addStatement("insert($L)",firstLowerClassName).
	        		     endControlFlow().
	        		     build();
	          methodSpecs.add(batchInsert);
	          MethodSpec batchDelete=MethodSpec.methodBuilder("batchDelete").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(ClassUtil.getArrayClassByClass(primaryKey_class), "primaryKeys").
	        		  beginControlFlow("for($T primaryKey:primaryKeys)", primaryKey_class).
	        		  addStatement("delete(primaryKey)").
	        		  endControlFlow().
	        		  build();
	          methodSpecs.add(batchDelete);
	          
	          ClassName id_ClassName=ClassName.get(primaryKey_class);
	          TypeName list_idType=ParameterizedTypeName.get(list_,id_ClassName);
	          MethodSpec batchDelete2=MethodSpec.methodBuilder("batchDelete").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(list_idType, "primaryKeys").
	        		  beginControlFlow("for($T primaryKey:primaryKeys)", primaryKey_class).
	        		  addStatement("delete(primaryKey)").
	        		  endControlFlow().
	        		  build();
	          methodSpecs.add(batchDelete2);
	          
	          MethodSpec batchUpdate=MethodSpec.methodBuilder("batchUpdate").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(TypeNameListChangeObj, firstLowerClassName+"s").
	        		  beginControlFlow("for($T $L:$L)", clazz,firstLowerClassName,firstLowerClassName+"s").
	        		  addStatement("update($L)",firstLowerClassName).
	        		  endControlFlow().
	        		  build();
	          methodSpecs.add(batchUpdate);
	          
	          MethodSpec batchSelect=MethodSpec.methodBuilder("batchSelect").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(TypeNameListChangeObj).
	        		  addParameter(ClassUtil.getArrayClassByClass(primaryKey_class), "primaryKeys").
	        		  addStatement("$T $L=new $T<>()",TypeNameListChangeObj,firstLowerClassName+"s",ArrayList.class).
	        		  beginControlFlow("for($T primaryKey:primaryKeys)",primaryKey_class).
	        		  addStatement("$L.add(select(primaryKey))",firstLowerClassName+"s").
	        		  endControlFlow().
	        		  addStatement("return $L",firstLowerClassName+"s").
	        		  build();
	          methodSpecs.add(batchSelect);
	          
	          MethodSpec batchSelect2=MethodSpec.methodBuilder("batchSelect").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(TypeNameListChangeObj).
	        		  addParameter(list_idType, "primaryKeys").
	        		  addStatement("$T $L=new $T<>()",TypeNameListChangeObj,firstLowerClassName+"s",ArrayList.class).
	        		  beginControlFlow("for($T primaryKey:primaryKeys)",primaryKey_class).
	        		  addStatement("$L.add(select(primaryKey))",firstLowerClassName+"s").
	        		  endControlFlow().
	        		  addStatement("return $L",firstLowerClassName+"s").
	        		  build();
	          methodSpecs.add(batchSelect2);
	          
	           //work 根据条件删查
	          ClassName condition=ClassName.get(prop.getProperty("targetPackage")+"."+className+"Mapper","Condition");
	          
	          MethodSpec deleteByCondition=MethodSpec.methodBuilder("deleteByCondition").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(condition, "condition").
	        		  addStatement("$T list=selectByCondition(condition)",TypeNameListChangeObj).
	        		  beginControlFlow("for($T $L:list) ",clazz,firstLowerClassName).
	        		  addStatement("delete(($T)$T.callPKGetMethod($L))",primaryKey_class,ColumnUtil.class,firstLowerClassName).
	        		  endControlFlow().
	        		  build();
	          methodSpecs.add(deleteByCondition);
	          
	          MethodSpec selectByCondition=MethodSpec.methodBuilder("selectByCondition").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(TypeNameListChangeObj).
	        		  addParameter(condition, "condition").
	        		  addStatement("String sql=SqlUtil.getSelectSql($L.class, condition!=null?condition.generateCondition():null)",className).
	        		  addStatement("List<$L> objs=new ArrayList<>()",className).
	        		  addStatement("List<Object> list=SqlUtil.select(sql, $L.class,condition!=null?condition.generateParams():null)",className).
	        		  beginControlFlow("for(Object object:list)").
	        		  addStatement("objs.add(($L)object)",className).
	        		  endControlFlow().
	        		  addStatement("return objs").
	        		  build();
	          methodSpecs.add(selectByCondition);
	          
	          ClassName pageInfo_=ClassName.get(PageInfo.class);
	          TypeName pageInfo_clazz=ParameterizedTypeName.get(pageInfo_, clazz);
	          MethodSpec selectByConditionWithPagination=MethodSpec.methodBuilder("selectByConditionWithPagination").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(pageInfo_clazz).
	        		  addParameter(condition, "condition").
	        		  addParameter(pageInfo_clazz, "pageInfo").
	        		  addStatement("String sql=SqlUtil.getSelectSql($L.class, condition!=null?condition.generateCondition():null)",className).
	        		  addStatement("List<$L> objs=new ArrayList<>()",className).
	        		  addStatement("List<Object> list=SqlUtil.select(sql, $L.class,condition!=null?condition.generateParams():null)",className).
	        		  beginControlFlow("for(Object object:list)").
	        		  addStatement("objs.add(($L)object)",className).
	        		  endControlFlow().
	        		  addCode(" pageInfo.setPageRecordCount(objs.size());\r\n" + 
	        		  		"	  List<$L> temp=new ArrayList<>();\r\n" + 
	        		  		"			  int currentPage=pageInfo.getCurPage();\r\n" + 
	        		  		"			  int maxPageSize=pageInfo.getPageSize();\r\n" + 
	        		  		"			  int index=0;\r\n" + 
	        		  		"			  int start=(currentPage-1)*maxPageSize;\r\n" + 
	        		  		"			  for($L $L:objs) {\r\n" + 
	        		  		"				 if(index>=start&&index<start+maxPageSize) {\r\n" + 
	        		  		"					 temp.add($L);\r\n" + 
	        		  		"				 }\r\n" + 
	        		  		"				 index++;\r\n" + 
	        		  		"			  }\r\n" + 
	        		  		"	  pageInfo.setList(temp);\r\n" + 
	        		  		"	  return pageInfo;",className,className,StringUtil.firstLetterLower(className),StringUtil.firstLetterLower(className)).
	        		  build();
	          methodSpecs.add(selectByConditionWithPagination);
	          
	         //work 以sql语句进行增删该查--但不推荐使用
	          MethodSpec insertBySql=MethodSpec.methodBuilder("insertBySql").
	        		  addAnnotation(Deprecated.class).
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(String.class, "sql").
	        		  addParameter(clazz,firstLowerClassName).
	        		  addStatement("SqlUtil.save(sql, $L)",firstLowerClassName).
	        		  build();
	          methodSpecs.add(insertBySql);
	          
	          
	          MethodSpec deleteBySql=MethodSpec.methodBuilder("deleteBySql").
	        		  addAnnotation(Deprecated.class).
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(String.class, "sql_").
	        		  addParameter(Object[].class, "params").
	        		  addStatement("String[] arrStr=sql_.split(\"where\")").
	        		  addStatement("String sql=SqlUtil.getSelectSql($L.class, null)",className).
	        		  addStatement("sql+=\" where \"+arrStr[1]").
	        		  addStatement("List<Object> list=SqlUtil.select(sql,$L.class, params)",className).
	        		  beginControlFlow("for(Object object:list)").
	        		  addStatement("SqlUtil.delete(SqlUtil.getDeleteSql($L.class)+\"where \"+ClassUtil.getPrimaryKeyByClass($L.class)+\"=?\", ($T)$T.callPKGetMethod(object))",className,className,primaryKey_class,ColumnUtil.class).
	        		  endControlFlow().
	        		  build();
	          methodSpecs.add(deleteBySql);
	          
	          MethodSpec updateBySql=MethodSpec.methodBuilder("updateBySql").
	        		  addAnnotation(Deprecated.class).
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(void.class).
	        		  addParameter(String.class, "sql").
	        		  addParameter(Object[].class,"params").
	        		  addStatement("SqlUtil.modify(sql, params)").
	        		  build();
	          methodSpecs.add(updateBySql);
	          
	          MethodSpec selectBySql=MethodSpec.methodBuilder("selectBySql").
	        		  addAnnotation(Deprecated.class).
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(TypeNameListChangeObj).
	        		  addParameter(String.class, "sql").
	        		  addParameter(Object[].class,"params").
	        		  addStatement("$T list=new ArrayList<>()",TypeNameListChangeObj).
	        		  addStatement("List<Object> temps=SqlUtil.select(sql, $L.class, params)",className).
	        		  beginControlFlow("for(Object temp:temps)").
	        		  addStatement("list.add(($L)temp)",className).
	        		  endControlFlow().
	        		  addStatement("return list").
	        		  build();
	          methodSpecs.add(selectBySql);
	          
	          //创建条件的方法
	          MethodSpec createCondtion=MethodSpec.methodBuilder("createCondtion").
	        		  addModifiers(Modifier.PUBLIC).
	        		  returns(condition).
	        		  addStatement("return new $T()",condition).
	        		  build();
	          methodSpecs.add(createCondtion);
	          
	          
	          
	          //work 创建Condition---------------------------------------
	               com.squareup.javapoet.TypeSpec.Builder conditionTypeBuilder=TypeSpec.classBuilder(condition).
	            		   addModifiers(Modifier.PUBLIC).
	            		   addModifiers(Modifier.STATIC);
	               //属性
	               ClassName string_=ClassName.get(String.class);
	               ClassName integer_=ClassName.get(Integer.class);
	               ClassName map_=ClassName.get(Map.class);
	               TypeName map_int_string=ParameterizedTypeName.get(map_, integer_,string_);
	               
	                FieldSpec paramCount=FieldSpec.builder(int.class,"paramCount", Modifier.PRIVATE).initializer("0").build();
	                FieldSpec params=FieldSpec.builder(classNameListObject,"params", Modifier.PRIVATE).initializer("new ArrayList<>()").build();
	                FieldSpec sql=FieldSpec.builder(StringBuffer.class,"sql", Modifier.PRIVATE).initializer("new StringBuffer()").build();
	                FieldSpec cache=FieldSpec.builder(map_int_string,"cache", Modifier.PRIVATE).initializer("new $T()",HashMap.class).build();
	                //构造方法
	                MethodSpec havaParamConstructor=MethodSpec.constructorBuilder().
	                		addParameter(int.class,"paramCount").
	                		addParameter(classNameListObject,"params").
	                		addParameter(StringBuffer.class,"sql").
	                		addStatement("this.paramCount = paramCount").
	                		addStatement("this.params = params").
	                		addStatement("this.sql = sql").
	                		build();
	                
	                MethodSpec noParamConstructor=MethodSpec.constructorBuilder().build();
	                		
	                                         	                		             
	                //work 成员方法
	                MethodSpec simplify=MethodSpec.methodBuilder("simplify").
	  	        		  addModifiers(Modifier.PRIVATE).
	  	        		  addParameter(String.class,"str").
	  	        		  addParameter(Object[].class,"params").
	  	        		  returns(condition).
	  	        		  addStatement("this.paramCount++").
	  	        		  beginControlFlow("if(params!=null)").
	  	        		       beginControlFlow("for(Object param:params) ").
	  	        		       addStatement("this.params.add(param)").
	  	        		       endControlFlow().
	  	        		  endControlFlow().
	  	        		  beginControlFlow("if(paramCount>1&&!StringUtil.isLastKeywordEqualToOR(sql))").
	  	        		  addStatement("this.sql.append(\" and \"+str)").	  	        		  
	  	        		  nextControlFlow("else").
	  	        		  addStatement("this.sql.append(str)").
	  	        		  addStatement("return this").
	  	        		  endControlFlow().
	  	        		  addStatement("return this").
	  	        		  build();
	                
	                MethodSpec getSqlWithOutWhere=MethodSpec.methodBuilder("getSqlWithOutWhere").
	                		addModifiers(Modifier.PUBLIC).
	                		returns(String.class).
	                		addStatement("return sql.toString()").
	                		build();
	                
	                MethodSpec generateParams=MethodSpec.methodBuilder("generateParams").
	                		addModifiers(Modifier.PUBLIC).
	                		returns(Object[].class).
	                		addStatement("return params.toArray()").
	                		build();
	                
	                MethodSpec includ=MethodSpec.methodBuilder("includ").
	                		addModifiers(Modifier.PUBLIC).
	                		addParameter(condition,"condition").
	                		returns(condition).
	                		addStatement("this.paramCount+=condition.paramCount").
	                		addStatement("this.sql.append(\"(\"+condition.sql+\")\")").
	                		addStatement("this.params.addAll(condition.params)").
	                		addStatement("return this").
	                		build();
	                
	                
	                MethodSpec OR=MethodSpec.methodBuilder("OR").
	                		addModifiers(Modifier.PUBLIC).
	                		returns(condition).
	                		addStatement("this.sql.append(\" OR \")").
	                		addStatement("return this").
	                		build();
	                MethodSpec generateCondition=MethodSpec.methodBuilder("generateCondition").
	                		addModifiers(Modifier.PUBLIC).
	                		returns(String.class).
	                		beginControlFlow("if(paramCount==0)").
	                		   addStatement("return sql.toString()").
	                		nextControlFlow("else").
	                		   addStatement("return \"where \"+sql.toString()+StringUtil.parseCache(cache)").
	                		endControlFlow().   
	                		build();
	                
	                MethodSpec DESC=MethodSpec.methodBuilder("DESC").
	                		addModifiers(Modifier.PUBLIC).
	                		addAnnotation(Deprecated.class).
	                		returns(condition).
	                		addStatement("this.cache.put(2,\" DESC \")").
	                		addStatement("return this").
	                		build();
	                
	                MethodSpec orderBy=MethodSpec.methodBuilder("orderBy").
	                		addModifiers(Modifier.PUBLIC).
	                		addParameter(Object.class,"val").
	                		returns(condition).
	                		addStatement("this.cache.put(1,\" order By \"+val+\" \")").
	                		addStatement("return this").
	                		build();
	                
	                MethodSpec orderBy2=MethodSpec.methodBuilder("orderBy").
	                		addModifiers(Modifier.PUBLIC).
	                		addJavadoc("第一个参数是参与排序字段名称(String)，第二参数为false时为降序排序(boolean)").
	                		addParameter(Object.class,"val").
	                		addParameter(Object.class,"flag").
	                		returns(condition).
	                		addStatement("this.cache.put(1,\" order By \"+val+\" \")").
	                		beginControlFlow("if(!(Boolean)flag)").
	                		addStatement("$N()",DESC).
	                		endControlFlow().
	                		addStatement("return this").
	                		build();
	                
	                MethodSpec limit=MethodSpec.methodBuilder("limit").
	                		addModifiers(Modifier.PUBLIC).
	                		addParameter(long.class,"start").
	                		addParameter(long.class,"end").
	                		returns(condition).
	                		addStatement("this.cache.put(3,\" limit \" +start+\" , \"+end)").
	                		addStatement("return this").
	                		build();
	              //work condtion添加方法
		             conditionTypeBuilder.addMethod(limit);   
		             conditionTypeBuilder.addMethod(OR);   
		             conditionTypeBuilder.addMethod(orderBy);   
		             conditionTypeBuilder.addMethod(orderBy2);
		             conditionTypeBuilder.addMethod(includ);
		             conditionTypeBuilder.addMethod(DESC);
		             conditionTypeBuilder.addMethod(havaParamConstructor);   
		             conditionTypeBuilder.addMethod(noParamConstructor);   
		             conditionTypeBuilder.addMethod(simplify);   
		             conditionTypeBuilder.addMethod(getSqlWithOutWhere);   
		             conditionTypeBuilder.addMethod(generateCondition);   
		             conditionTypeBuilder.addMethod(generateParams); 
		             
		             conditionTypeBuilder.addField(sql);
		             conditionTypeBuilder.addField(paramCount);
		             conditionTypeBuilder.addField(params);
		             conditionTypeBuilder.addField(cache);
	             //work 需要便利的方法
	                TableInfo tableInfo=GlobalParams.ClassName2TableMap.get(clazz);
	                List<ColumnInfo> columnInfos=tableInfo.getCloumnInfoList();
	             for(ColumnInfo columnInfo:columnInfos) {
	            	 String columnName=columnInfo.getName();
	            	 String methodName=ClassUtil.getClassSimpleName(columnName);
	            	 columnName="\""+columnName+"\"";
	            	 //columnName="$T.getFieldName($L.class,\""+columnName+"\")";
	            	 MethodSpec method1=MethodSpec.methodBuilder("and"+methodName+"IsNull").
		                		addModifiers(Modifier.PUBLIC).
		                		returns(condition).
		                		addStatement("return simplify("+columnName+"+\" is null \",null)",ColumnUtil.class,className).
		                		build();
	            	 
	            	 MethodSpec method2=MethodSpec.methodBuilder("and"+methodName+"NotNull").
	            			 addModifiers(Modifier.PUBLIC).
	            			 returns(condition).
	            			 addStatement("return simplify("+columnName+"+\" is not null \",null)",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method3=MethodSpec.methodBuilder("and"+methodName+"EqualTo").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement("return simplify("+columnName+"+\" = ? \",new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method4=MethodSpec.methodBuilder("and"+methodName+"NotEqualTo").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement("return simplify("+columnName+"+\" != ? \",new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method5=MethodSpec.methodBuilder("and"+methodName+"GreaterThan").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement("return simplify("+columnName+"+\" > ? \", new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method6=MethodSpec.methodBuilder("and"+methodName+"GreaterThanOrEqualTo").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement("return simplify( "+columnName+"+\" >= ? \", new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method7=MethodSpec.methodBuilder("and"+methodName+"LessThan").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement("return simplify( "+columnName+"+\" < ? \", new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method8=MethodSpec.methodBuilder("and"+methodName+"LessThanOrEqualTo").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement(" return simplify("+columnName+"+\" <= ? \", new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method9=MethodSpec.methodBuilder("and"+methodName+"Like").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement("return simplify( "+columnName+"+\" like ? \", new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method10=MethodSpec.methodBuilder("and"+methodName+"NotLike").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"val").
	            			 returns(condition).
	            			 addStatement("return simplify( "+columnName+"+\" not like ? \", new Object[]{val})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method11=MethodSpec.methodBuilder("and"+methodName+"In").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(classNameListObject,"list").
	            			 returns(condition).
	            			 addStatement("this.params.addAll(list)").
	            			 addStatement("$T sb=new StringBuffer()",StringBuffer.class).
	            			 beginControlFlow("for(Object object:list)").
	            			     addStatement("sb.append(\"?,\")").
	            			 endControlFlow().
	            			 addStatement("$T.clearEndChar(sb)",StringUtil.class).
	            			 addStatement("return simplify("+columnName+"+\" in (\"+sb.toString()+\")\",null)",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method12=MethodSpec.methodBuilder("and"+methodName+"NotIn").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(classNameListObject,"list").
	            			 returns(condition).
	            			 addStatement("this.params.addAll(list)").
	            			 addStatement("$T sb=new StringBuffer()",StringBuffer.class).
	            			 beginControlFlow("for(Object object:list)").
	            			 addStatement("sb.append(\"?,\")").
	            			 endControlFlow().
	            			 addStatement("StringUtil.clearEndChar(sb)").
	            			 addStatement("return simplify("+columnName+"+\" not in (\"+sb.toString()+\")\",null)",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method13=MethodSpec.methodBuilder("and"+methodName+"BetweenTo").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"start").
	            			 addParameter(Object.class,"end").
	            			 returns(condition).
	            			 addStatement("return simplify("+columnName+"+\" between ? and ?\",new Object[]{start,end})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 MethodSpec method14=MethodSpec.methodBuilder("and"+methodName+"NotBetweenTo").
	            			 addModifiers(Modifier.PUBLIC).
	            			 addParameter(Object.class,"start").
	            			 addParameter(Object.class,"end").
	            			 returns(condition).
	            			 addStatement("return simplify("+columnName+"+\" not between ? and ?\",new Object[]{start,end})",ColumnUtil.class,className).
	            			 build();
	            	 
	            	 conditionTypeBuilder.addMethod(method1);
	            	 conditionTypeBuilder.addMethod(method2);
	            	 conditionTypeBuilder.addMethod(method3);
	            	 conditionTypeBuilder.addMethod(method4);
	            	 conditionTypeBuilder.addMethod(method5);
	            	 conditionTypeBuilder.addMethod(method6);
	            	 conditionTypeBuilder.addMethod(method7);
	            	 conditionTypeBuilder.addMethod(method8);
	            	 conditionTypeBuilder.addMethod(method9);
	            	 conditionTypeBuilder.addMethod(method10);
	            	 conditionTypeBuilder.addMethod(method11);
	            	 conditionTypeBuilder.addMethod(method12);
	            	 conditionTypeBuilder.addMethod(method13);
	            	 conditionTypeBuilder.addMethod(method14);
	             }
	             
	             
	     //work 填充进类中        
	     typeTemp.addMethods(methodSpecs);        
	     typeTemp.addType(conditionTypeBuilder.build());
	     
	     JavaFile javaFile=JavaFile.builder(prop.getProperty("targetPackage"), typeTemp.build()).build();
	     try {
			javaFile.writeTo(new File(prop.getProperty("targetProject")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	}
		  
		
	}

	/**
	 * 生成java文件
	 */
	public static void generateJavaFile(String configPath){
		  Properties prop=GlobalParams.properties;
		//如果没有的话则生存对应的包
		  File directory=new File(prop.getProperty("targetProject")+"\\"+StringUtil.spot2Slash(prop.getProperty("targetPackage")));
		  if(!directory.exists()) {
			  directory.mkdirs();
		  }
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
					name=ClassUtil.getClassSimpleName(name);
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
				//toString方法
				Builder toString=MethodSpec.methodBuilder("toString").
						addAnnotation(Override.class).
						returns(String.class).
						addModifiers(Modifier.PUBLIC);
				 toString.addCode("return \"[\"+");
				 int count=0;
			     for(ColumnInfo columnInfo:tableInfo.getCloumnInfoList()) {
			    	    count++;
			    	    if(count==tableInfo.getCloumnInfoList().size()) {
			    	    	toString.addStatement("$S+\"=\"+this.$L+\"]\"",columnInfo.getName(),columnInfo.getName());
			    	    }else {
			    	    	toString.addCode("$S+\"=\"+this.$L+\",\"+",columnInfo.getName(),columnInfo.getName());
			    	    }
			     }
				
				
				//将属性与方法添加进类中
				String simpleClassName=ClassUtil.getClassSimpleName(tableInfo.getTname());
				TypeSpec type=TypeSpec.classBuilder(simpleClassName)
				.addMethods(methods)
				.addMethod(toString.build())
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
	
	
	
	
	
	
	
	
	
	
}
