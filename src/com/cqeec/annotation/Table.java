package com.cqeec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})//可以定义在属性上
@Retention(RetentionPolicy.RUNTIME)//运行有效,存在class字节码文件中
public @interface Table {
   String value();
}
