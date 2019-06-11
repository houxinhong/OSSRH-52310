package com.cqeec.util.core;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.cqeec.annotation.Id;
import com.cqeec.annotation.Table;
import com.cqeec.bean.TableInfo;
import com.cqeec.core.GlobalParams;
import com.cqeec.core.MySqlTypeConvertor;
import com.cqeec.util.other.FileParseUtil;
import com.cqeec.util.other.StringUtil;
import com.squareup.javapoet.ClassName;



public class ClassUtil {
	
	/**
	 * 根据Class对象获取简单类名
	 * （如com.cqeec.pojo.User--->User）
	 * @param clazz
	 * @return
	 */
	public static String getClassSimpleName(Class clazz) {
		String qualifiedName=clazz.getName();
		String[] names=qualifiedName.split("\\.");
		return names[names.length-1];
	}
	/**
	 * 根据表名生成简单类名
	 * @param tableName
	 * @return
	 */
	public static String getClassSimpleName(String tableName) {
		if(tableName.indexOf("_")!=-1) {
			String[] temps=tableName.split("_");
			StringBuffer sb=new StringBuffer();
			for(String temp:temps) {
				sb.append(StringUtil.firstLetterUpper(temp));
			}
			tableName=sb.toString();
		}else {
			tableName=StringUtil.firstLetterUpper(tableName);
		}
		return tableName;
	}
	
	
	/**
	 * 调用obj对象对应属性fieldName的get方法
	 * @param fieldName
	 * @param obj
	 * @return
	 */
	public static Object invokeGet(String fieldName,Object obj){
		try {
			Class c = obj.getClass();
			Method m = c.getDeclaredMethod("get"+StringUtil.firstLetterUpper(fieldName), null);
			return m.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * 调用set方法
	 * @param obj
	 * @param columnName
	 * @param columnValue
	 */
	public static void invokeSet(Object obj,String columnName,Object columnValue){
		try {
			Method m = obj.getClass().getDeclaredMethod("set"+StringUtil.firstLetterUpper(columnName), 
					columnValue.getClass());
			m.invoke(obj, columnValue);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	public static String getQuilifiedName(String tname) {
		String simple=getClassSimpleName(tname);
		String  quilified=FileParseUtil.parsePropertyFile("config.properties").getProperty("targetPackage")+"."+simple;
		return quilified;
	}
	public static List<ClassName> getClassNameList(Properties prop) {
		
		List<TableInfo> list=TableUtil.getTables();
		List<ClassName> list2=new ArrayList<>();
		for(TableInfo tableInfo:list) {
		    ClassName className=ClassName.get(prop.getProperty("targetPackage"),ClassUtil.getClassSimpleName(tableInfo.getTname())); 
			list2.add(className);
			GlobalParams.getClassName2TableMap().put(className,tableInfo);
		}
		return list2;
	}
	
	public static Map<ClassName, TableInfo> getClassName_tableInfoMap(Properties prop){
		List<TableInfo> list=TableUtil.getTables();
		List<ClassName> list2=new ArrayList<>();
		Map<ClassName, TableInfo> map=new HashMap<>();
		for(TableInfo tableInfo:list) {
		    ClassName className=ClassName.get(prop.getProperty("targetPackage"),ClassUtil.getClassSimpleName(tableInfo.getTname())); 
			list2.add(className);
			map.put(className,tableInfo);
		}
		return map;
	}
	
	
	
	public static Class getTypeByFieldNameAndClassName(String string, ClassName clazz) {
		String columnType=GlobalParams.getClassName2TableMap().get(clazz).getColumns().get(string).getDataType();
		return MySqlTypeConvertor.databaseType2JavaType(columnType);
	}
	
	
	public static Class getArrayClassByClass(Class clazz) {
		//-------------------
		if(clazz.equals(String.class)) {
			return String[].class;
		}
		if(clazz.equals(Integer.class)) {
			return Integer[].class;
			
		}
		if(clazz.equals(Boolean.class)) {
			return Boolean[].class;
			
		}
		if(clazz.equals(Long.class)) {
			return Long[].class;
			
		}
		if(clazz.equals(Double.class)) {
			return Double[].class;
			
		}
		return null;
	}
	/**
	 * 由于加入类名与表名对应的注解所以这个方法应谨慎使用
	 * @param clazz
	 * @return
	 */
	public static String getPrimaryKeyByClassName(ClassName clazz) {
		return GlobalParams.getClassName2TableMap().get(clazz).getOnlyPriKey().getName();
	}
	
	public static String getPrimaryKeyByClass(Class clazz) {
		Field[] fields=clazz.getDeclaredFields();
		for(Field field:fields) {
			Id id=field.getDeclaredAnnotation(Id.class);
			if(id!=null) {
				return id.value(); 
			}
		}
		Table table=(Table) clazz.getDeclaredAnnotation(Table.class);
		if(table==null) {
			return getPrimaryKeyByClassName(ClassName.get(clazz));
		}else {
			String tableName=table.value();
			String simpleName=getClassSimpleName(tableName);
			return getPrimaryKeyByClassName(ClassName.get(GlobalParams.getProperties().getProperty("targetPackage"), simpleName));
		}
	}
	public static String getPrimaryKeyFieldName(Class clazz) {
		Field[] fields=clazz.getDeclaredFields();
		for(Field field:fields) {
			if(field.getDeclaredAnnotation(Id.class)!=null) {
				return field.getName();
			}
		}
		return getPrimaryKeyByClass(clazz);
		
	}
	@Deprecated
	public static Class getClassByTableInfo(TableInfo tableInfo) {
		String simpleName=getClassSimpleName(tableInfo.getTname());
		try {
			return Class.forName(GlobalParams.getProperties().getProperty("targetPackage")+"."+simpleName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new  RuntimeException("找不到与表对应的Class");
		}
	}
	
	//====================================================
	//         改良getClassByTableInfo                   //
	//====================================================
	  /**
     * 从包package中获取所有的Class
     * 
     * @param pack
     * @return
     */
    public static Set<Class<?>> getClasses(String pack) {

        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } 
                
                //暂时不知道以什么形式搞这个东西！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
                else if ("jar".equals(protocol)) {
                	
                	
                    // 如果是jar包文件
                    // 定义一个JarFile
                    // System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            // log
                                            // .error("添加用户自定义视图类错误
                                            // 找不到此类的.class文件");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }
    
    /**
     * 以文件的形式来获取包下的所有Class
     * 
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
            Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classes.add(Class.forName(packageName + '.' +
                    // className));
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(
                            Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
    
}
