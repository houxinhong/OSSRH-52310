package com.cqeec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.FIELD})//可以定义在属性上
@Retention(RetentionPolicy.RUNTIME)//运行有效,存在class字节码文件中
public @interface Column {
	/**
	 * 用于表字段与类属性进行映射
	 * @return
	 */
 String value();
}
