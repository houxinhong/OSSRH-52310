package com.cqeec.test;


import org.junit.Test;

import com.cqeec.pojo.Permission;
import com.cqeec.pojo.PermissionMapper;
import com.cqeec.pojo.PermissionMapper.Condition;
import com.cqeec.util.GenerateCodeUtil;

public class PermissionMapperTest {
	private PermissionMapper mapper=new PermissionMapper();
	
	@Test
	public void generate() {
		GenerateCodeUtil.generateJavaFile("config.properties");
		GenerateCodeUtil.generateMapper("config.properties");
	}

	@Test
	public void testInsert() {
		Permission permission=new Permission("test",15L, "1111","/dddd");
        mapper.insert(permission);		
	}

	@Test
	public void testDelete() {
		mapper.delete(15L);
	}

	@Test
	public void testUpdate() {
		Permission permission=mapper.select(12L);
		permission.setName("111111111222222");
		mapper.update(permission);
	}



	@Test
	public void testDeleteByCondition() {
		Condition condition=mapper.createCondtion().andIdDsdDsssEqualTo(12);
		mapper.deleteByCondition(condition);
	}

	@Test
	public void testSelectByCondition() {
		Condition condition=mapper.createCondtion().andIdDsdDsssEqualTo(11);
		Permission permission=mapper.selectByCondition(condition).get(0);
		System.out.println(permission);
	}

}
