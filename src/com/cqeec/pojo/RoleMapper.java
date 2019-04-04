package com.cqeec.pojo;

import java.util.ArrayList;
import java.util.List;

import com.cqeec.util.CollectionUtil;
import com.cqeec.util.SqlUtil;
import com.cqeec.util.StringUtil;

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
	//work 输出生成的sql语句
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
    public void insertBySql(String sql,Role role) {
		 SqlUtil.save(sql, role);
	}
	@Deprecated
	public void deleteBySql(String sql_,Object[] params) {
		String[] arrStr=sql_.split("where");
		String sql=SqlUtil.getSelectSql(Role.class, null);
		sql+=" where "+arrStr[1];
		List<Object> list=SqlUtil.select(sql,Role.class, params);
		for(Object object:list) {
			SqlUtil.delete(SqlUtil.getDeleteSql(Role.class)+"where id = ?", ((Role)object).getId());
		}
	}
	@Deprecated
	public void updateBySql(String sql,Object[] params) {
		SqlUtil.modify(sql, params);
	}
	@Deprecated
	public List<Role> selectBySql(String sql,Object[] params) {
		List<Role> list=new ArrayList<>();
		List<Object> temps=SqlUtil.select(sql, Role.class, params);
		for(Object temp:temps) {
			list.add((Role)temp);
		}
		return list;
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
         * 简化一些重复的操作
         */
		private  Condition simplify(String str,Object[]params) {
			this.paramCount++;
			if(params!=null) {
				for(Object param:params) {
					this.params.add(param);
				}
			}
			if(paramCount==1) {
           	 this.sql.append(str);	
            }else {
           	 this.sql.append(" and "+str);	
            }
			 return this;
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
				return sql.toString();
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
		//排序关键字DESC
		public Condition DESC() {
			this.sql.append(" DESC ");
			return this;
		}
		//or关键字
		public Condition Or() {
			this.sql.append(" OR ");
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
		public Condition andIdIsNull() {
			return simplify(" id is null ",null);
        }

        public Condition andIdIsNotNull() {
        	return simplify(" id is not null ",null);
        }
		
		public Condition andIdEqualTo(Object val) {
			return simplify(" id = ? ",new Object[]{val}) ;
		}
		
		public Condition andIdNotEqualTo(Object val) {
			return simplify("  id != ? ",new Object[]{val});
		}
		
		public Condition andIdGreaterThan(Object val) {
			return simplify(" id > ? ", new Object[]{val});
		}
		
		public Condition andIdGreaterThanOrEqualTo(Object val) {
			return simplify(" id >= ? ", new Object[]{val});
		}
		
		public Condition andIdLessThan(Object val) {
			return simplify(" id < ? ", new Object[]{val});
		}
		
		public Condition andIdLessThanOrEqualTo(Object val) {
            return simplify(" id <= ? ", new Object[]{val});			
		}

		public Condition andIdLike(Object val) {
			return simplify(" id like ? ", new Object[]{val});
		}
		
		public Condition andIdNotLike(Object val) {
			return simplify(" id not like ? ", new Object[]{val});
		}
		
		public Condition andIdIn(List<Object> list ) {
			this.params.addAll(list);
			StringBuffer sb=new StringBuffer();
			for(Object object:list) {
				sb.append("?,");
			}
			StringUtil.clearEndChar(sb);
			return simplify(" id in ("+sb.toString()+")",null);
		}
		
		public Condition andIdNotIn(List<Object> list) {
			this.params.addAll(list);
			StringBuffer sb=new StringBuffer();
			for(Object object:list) {
				sb.append("?,");
			}
			StringUtil.clearEndChar(sb);
			return simplify(" id not in ("+sb.toString()+")",null);
		}
		
		public Condition andIdBetweenTo(Object start,Object end){
			return simplify(" id between ? and ?",new Object[]{start,end});
		}
		
		public Condition andIdNotBetweenTo(Object start,Object end){
			return simplify("id not between ? and ?",new Object[]{start,end});
		}
		//-------------------------------------------

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
