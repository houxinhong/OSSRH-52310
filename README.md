# orm

#### 介绍
如果使用2.0标签版本，改变属性名称，重新创建setter getter方法的时候需要检查一下,需要符合相应的规范，
例如desc_属性 getDesc方法  每次使用的时候自己先测试一下，生成自己使用的测试类 注意包名不能与com.cqeec重复 可在orm中参考测试方法
注意targetProject的配置和工程有关，maven和普通web项目是不一样的