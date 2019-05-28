package com.cqeec.test;


import java.util.List;

import org.junit.Test;

import com.cqeec.bean.PageInfo;
import com.cqeec.pojo.Permission;
import com.cqeec.pojo.PermissionMapper;
import com.cqeec.pojo.RoleMapper;
import com.cqeec.pojo.RolePermission;
import com.cqeec.pojo.PermissionMapper.Condition;
import com.cqeec.pojo.Role;
import com.cqeec.util.ColumnUtil;
import com.cqeec.util.GenerateCodeUtil;
import com.cqeec.util.TableUtil;

public class PermissionMapperTest {
	private PermissionMapper mapper=new PermissionMapper();
	@Test
	public void generate() {
		GenerateCodeUtil.generateJavaFile("config.properties");
		GenerateCodeUtil.generateMapper("config.properties");
	}

	@Test
	public void insert() {
		Permission permission=new Permission("test",16L, "1111","/dddd");
	    mapper.insert(permission);		
	}

	@Test
	public void delete() {
		mapper.delete(15L);
	}

	@Test
	public void update() {
		Permission permission=mapper.select(2L);
		permission.setName("111661111222222");
		mapper.update(permission);
	}

	@Test
	public void select() {
		System.out.println(mapper.select(3L));
	}
	
	@Test
	public void deleteByCondition() {
		Condition condition=mapper.createCondtion().andIdEqualTo(2);
		mapper.deleteByCondition(condition);
	}
    
	@Test
	public void selectByCondition() {
		Condition condition=mapper.createCondtion().andIdEqualTo(3);
		Permission permission=mapper.selectByCondition(condition).get(0);
		System.out.println(permission);
	}
	
	@Test
	public void selectByConditionWithPagination() {
		PageInfo<Permission> pageInfo=mapper.selectByConditionWithPagination(null,new PageInfo<>(1, 3));
		System.out.println(pageInfo.getStartPageButton());
		System.out.println(pageInfo.getEndPageButton());
		System.out.println(pageInfo.getList());
		System.out.println(pageInfo.getPageNumber());
	}
	
	
	@Test
	public void tableAndColumnAnnotation() throws NoSuchFieldException, SecurityException {
		System.out.println(TableUtil.getTableNameByClass(RolePermission.class));
		System.out.println(TableUtil.getTableNameByClass(Permission.class));
		System.out.println(ColumnUtil.getColumnNameByField(Permission.class.getDeclaredField("name")));
	}
	
	
}
