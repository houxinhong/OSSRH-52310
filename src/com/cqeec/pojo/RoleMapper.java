package com.cqeec.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cqeec.util.CollectionUtil;
import com.cqeec.util.SqlUtil;

public class RoleMapper {
	//常用的增删改查
	public void insert(Role role) {
	        String sql=SqlUtil.getInsertSql(role.getClass()); 
	        SqlUtil.save(sql,role);
	}
	
	public void delete(long id) {
			String sql=SqlUtil.getDeleteSql(Role.class)+"where id=?";
			SqlUtil.delete(sql,id);
	}
	
	public void update(Role role) {
		String sql=SqlUtil.getUpdateSql(role.getClass());
		SqlUtil.modify(sql,CollectionUtil.sortByUpdate(role));
		
	}
	
	public Role select(long id) {
		String sql=SqlUtil.getSelectSql(Role.class, "where id=?");
		return SqlUtil.select(sql,Role.class,id)!=null?(Role)SqlUtil.select(sql,Role.class,id).get(0):null;
	}
	//批量增删改查
    public void batchInsert(List<Role> roles) {
		for(Role role:roles) {
			insert(role);
		}
	}
	
	public void batchDelete(long[] ids) {
		for(long id:ids) {
			delete(id);
		}
	}
	
	public void batchUpdate(List<Role> roles) {
		for(Role role:roles) {
			update(role);
		}
	}
	
	public List<Role> batchSelect(long[] ids) {
		List<Role> roles=new ArrayList<>();
		for(long id:ids) {
			roles.add(select(id));
		}
		return roles;
	}
	//根据条件删查
	public void deleteByCondition(Condition condition) {
		 List<Role> list=selectByCondition(condition);
		 for(Role role:list) {
			 delete(role.getId());
		 }
	}
	
	public List<Role> selectByCondition(Condition condition) {
		String sql=SqlUtil.getSelectSql(Role.class, condition!=null?condition.generateCondition():null);
		List<Role> roles=new ArrayList<>();
		List<Object> list=SqlUtil.select(sql, Role.class,condition!=null?condition.generateParams():null);
		System.out.println(sql);
		for(Object object:list) {
			roles.add((Role)object);
		}
		return roles;
	}
	
	//以sql语句进行增删该查--但不推荐使用
	@Deprecated
    public void insertBySql(String sql) {
		
	}
	@Deprecated
	public void deleteBySql(String sql) {
		
	}
	@Deprecated
	public void updateBySql(String sql) {
		
	}
	@Deprecated
	public List<Role> selectBySql(String sql) {
		return null;
	}
	
	public Condition createCondtion() {
		return new Condition();
	}
	
	public static class Condition{
		private  int paramCount=0;
		private List<Object> params=new ArrayList<>();
		private StringBuffer sql=new StringBuffer();
		
		private Condition(int paramCount, List<Object> params, StringBuffer sql) {
			this.paramCount = paramCount;
			this.params = params;
			this.sql = sql;
		}
		
		private Condition() {
			
		}

        
		
		/**
		 * 获取没有带where关键字的sql语句
		 * @return
		 */
		public String getSqlWithOutWhere() {
			return sql.toString();
		}
		/**
         *生成sql条件 
         * @return
         */
		public String generateCondition() {
			if(paramCount==0) {
				return ""+sql.toString();
			}else {
				return "where "+sql.toString();
			}
		}
		/**
		 * 生成sql语句所需参数
		 * @return
		 */
		public Object[] generateParams() {
			return params.toArray();
		}
		/*
		public Condition or() {
			this.sql.append(" or ");
			return this;
		}
		
		public Condition and() {
			this.sql.append(" and ");
			return this;
		}
		
		
		public Condition greaterThan(long val) {
			this.paramCount++;
			this.sql.append(" > ? ");
			this.params.add(val);
			return this;
		}
		
		public Condition lessThan(long val) {
			this.paramCount++;
			this.sql.append(" < ? ");
			this.params.add(val);
			return this;
		}
		
		public Condition equal(Object val) {
			this.paramCount++;
			this.sql.append(" = ? ");
			this.params.add(val);
			return this;
		}
		
		public Condition like(Object val) {
			this.paramCount++;
			this.sql.append(" like ? ");
			this.params.add(val);
			return this;
		}
		
		public Condition id() {
			this.sql.append(" id ");
			return this;
		}
		public Condition name() {
			this.sql.append(" name ");
			return this;
		}
		public Condition desc_() {
			this.sql.append(" desc_ ");
			return this;
		}
		
		*/
		/**
		 * 包含，相当于括号
		 * @param condition
		 * @return
		 */
		public Condition includ(Condition condition) {
			this.paramCount+=condition.paramCount; 
			this.sql.append("("+condition.sql+")");
			this.params.addAll(condition.params);
			return this;
		}
		public Condition DESC() {
			this.sql.append(" DESC ");
			return this;
		}
		public Condition orderBy(Object val) {
			this.sql.append(" ORDER BY "+val);
			return this;
		}
		public Condition limit(long start,long num) {
			this.sql.append(" limit ?,? ");
			this.params.add(start);
			this.params.add(num);
			return this;
		}
		//----------------------------------------------------------------------------------
		public Condition andIdEquals(Object val) {
			 this.paramCount++;
			 this.params.add(val);
             if(paramCount==1) {
            	 this.sql.append(" id = ? ");	
             }else {
            	 this.sql.append(" and id = ? ");	
             }
			 return this;
		}
		public Condition andNameEquals(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name = ? ");	
			}else {
				this.sql.append(" and name = ? ");	
			}
			return this;
		}
		public Condition andDesc_Equals(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("desc_ = ? ");	
			}else {
				this.sql.append(" and desc_ = ? ");	
			}
			return this;
		}
		
		
		
		
		public Condition orIdEquals(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" id = ? ");	
			}else {
				this.sql.append(" or id = ? ");	
			}
			return this;
		}
		public Condition orNameEquals(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name = ? ");	
			}else {
				this.sql.append(" or name = ? ");	
			}
			return this;
		}
		public Condition orDesc_Equals(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("desc_ = ? ");	
			}else {
				this.sql.append(" or desc_ = ? ");	
			}
			return this;
		}
		
		
		public Condition andIdLike(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" id like ? ");	
			}else {
				this.sql.append(" and id like ? ");	
			}
			return this;
		}
		public Condition andNameLike(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name like ? ");	
			}else {
				this.sql.append(" and name like ? ");	
			}
			return this;
		}
		public Condition andDesc_Like(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("desc_ like ? ");	
			}else {
				this.sql.append(" and desc_ like ? ");	
			}
			return this;
		}
		
		public Condition orIdLike(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" id like ? ");	
			}else {
				this.sql.append(" or id like ? ");	
			}
			return this;
		}
		public Condition orNameLike(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name like ? ");	
			}else {
				this.sql.append(" or name like ? ");	
			}
			return this;
		}
		public Condition orDesc_Like(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" desc_ like ? ");	
			}else {
				this.sql.append(" or desc_ like ? ");	
			}
			return this;
		}
		
		
		public Condition andIdGreatThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" id > ? ");	
			}else {
				this.sql.append(" and id > ? ");	
			}
			return this;
		}
		public Condition andNameGreatThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name > ? ");	
			}else {
				this.sql.append(" and name > ? ");	
			}
			return this;
		}
		public Condition andDesc_GreatThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("desc_ > ? ");	
			}else {
				this.sql.append(" and desc_ > ? ");	
			}
			return this;
		}
		
		
		public Condition orIdGreatThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" id > ? ");	
			}else {
				this.sql.append(" or id > ? ");	
			}
			return this;
		}
		public Condition orNameGreatThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name > ? ");	
			}else {
				this.sql.append(" or name > ? ");	
			}
			return this;
		}
		public Condition orDesc_GreatThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("desc_ > ? ");	
			}else {
				this.sql.append(" or desc_ > ? ");	
			}
			return this;
		}
		
		
		
		public Condition andIdLessThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" id < ? ");	
			}else {
				this.sql.append(" and id < ? ");	
			}
			return this;
		}
		public Condition andNameLessThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name < ? ");	
			}else {
				this.sql.append(" and name < ? ");	
			}
			return this;
		}
		public Condition andDesc_LessThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("desc_ < ? ");	
			}else {
				this.sql.append(" and desc_ < ? ");	
			}
			return this;
		}
		
		
		public Condition orIdLessThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append(" id < ? ");	
			}else {
				this.sql.append(" or id < ? ");	
			}
			return this;
		}
		public Condition orNameLessThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("name < ? ");	
			}else {
				this.sql.append(" or name < ? ");	
			}
			return this;
		}
		public Condition orDesc_LessThan(Object val) {
			this.paramCount++;
			this.params.add(val);
			if(paramCount==1) {
				this.sql.append("desc_ < ? ");	
			}else {
				this.sql.append(" or desc_ < ? ");	
			}
			return this;
		}
		
		
		
	}
}
