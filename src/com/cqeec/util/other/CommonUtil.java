package com.cqeec.util.other;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class CommonUtil {

	
	/**
	 * 方法调用
	 * @param obj
	 * @param methodStr
	 * @param params
	 * @return
	 */
	public static Object invoke(Object obj,String methodStr,Object... params)  {
			try {
				//如果有参数则根据方法名与参数的Class进行获取method
				Method method=null;
				boolean flag=params!=null;
				int paramsCount=flag?params.length:0;
				Method[] methods=obj.getClass().getDeclaredMethods();

				//子类中有指定方法
				for(Method temp:methods) {
					//对于特殊方法的处理，因为condition指定类型处理起来比较麻烦
					if("selectByConditionWithPagination".equals(methodStr)&&methodStr.equals(temp.getName())) {
						method=temp;
						break;
					}
						
					//正常处理流程
					if(temp.getName().equals(methodStr)&&temp.getParameterCount()==paramsCount) {
						if(paramsCount>0) {
							Class[] classes=temp.getParameterTypes();
							int i;
							for(i=0;i<paramsCount;i++) {
								//判断方法参数类型与传入参数类型的顺序与类型是否一致
								if(classes[i]!=params[i].getClass())break;
							}
							//如果类型都一致则i==paramsCount
							if(i==paramsCount)method=temp; 
						}else {
							method=temp;
						}
					}
				} 
				//子类中没有指定方法(基于公共Controller或者Service类才会执行这里)
				if(method==null) {
					methods=obj.getClass().getSuperclass().getDeclaredMethods();
					for(Method temp:methods) {
						//方法名相同参数个数相同(虽然不能做到方法类型完全匹配，但可以做到参数个数的完全匹配)
						if(temp.getName().equals(methodStr)&&temp.getParameterCount()==paramsCount) {
							Type[] paramsType=temp.getGenericParameterTypes();
							//对于泛型方法参数这里不做判断
							if(paramsType.length!=0&&paramsType[0].getTypeName().equals("P")) {
								method=temp;
							}else if(paramsType.length==0){
								method=temp;
							}else {
								Class[] classes=temp.getParameterTypes();
								int i;
								for(i=0;i<paramsCount;i++) {
									if(classes[i]!=params[i].getClass())break;
								}
								if(i==paramsCount)method=temp;
							}
						}
					} 
				}
				
				       //如果父类中都没有找到则抛出异常
				       if(method==null)throw new RuntimeException("没有找到对应的方法："+methodStr);
				        //调用方法
						try {
 								if(flag) {
		                            return method.invoke(obj,params);
								}else {
									return method.invoke(obj);
								}
							//该段异常可能为mysql语法错误数据库无法连接等不可控异常
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new RuntimeException(e);
						}
			} catch ( SecurityException e) {
				//该异常为人为失误异常
				e.printStackTrace();
				return null;
			}
	}

}
