package com.cqeec.pojo;

import com.cqeec.bean.PageInfo;
import com.cqeec.util.ClassUtil;
import com.cqeec.util.CollectionUtil;
import com.cqeec.util.ColumnUtil;
import com.cqeec.util.DBUtil;
import com.cqeec.util.SqlUtil;
import com.cqeec.util.StringUtil;
import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleMapper {
  public static void enabletransaction() {
    DBUtil.enableTransaction();
  }

  public static void commit() {
    DBUtil.commitTransaction();
  }

  public void insert(Role role) {
    String sql=SqlUtil.getInsertSql(role.getClass());
    SqlUtil.save(sql,role);
  }

  public void delete(Long primaryKey) {
    String sql=SqlUtil.getDeleteSql(Role.class)+"where "+ClassUtil.getPrimaryKeyByClass(Role.class)+"=?";
    SqlUtil.delete(sql,primaryKey);
  }

  public void update(Role role) {
    String sql=SqlUtil.getUpdateSql(role.getClass())+"where "+ClassUtil.getPrimaryKeyByClass(Role.class)+"=?";
    SqlUtil.modify(sql,CollectionUtil.sortByUpdate(role));
  }

  public Role select(Long primaryKey) {
    String sql=SqlUtil.getSelectSql(Role.class, "where "+ClassUtil.getPrimaryKeyByClass(Role.class)+"=?");
    List<Object> temp=SqlUtil.select(sql,Role.class,primaryKey);
    return temp!=null&&temp.size()!=0?(Role)temp.get(0):null;
  }

  public void batchInsert(List<Role> roles) {
    for(Role role:roles) {
      insert(role);
    }
  }

  public void batchDelete(Long[] primaryKeys) {
    for(Long primaryKey:primaryKeys) {
      delete(primaryKey);
    }
  }

  public void batchDelete(List<Long> primaryKeys) {
    for(Long primaryKey:primaryKeys) {
      delete(primaryKey);
    }
  }

  public void batchUpdate(List<Role> roles) {
    for(Role role:roles) {
      update(role);
    }
  }

  public List<Role> batchSelect(Long[] primaryKeys) {
    List<Role> roles=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      roles.add(select(primaryKey));
    }
    return roles;
  }

  public List<Role> batchSelect(List<Long> primaryKeys) {
    List<Role> roles=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      roles.add(select(primaryKey));
    }
    return roles;
  }

  public void deleteByCondition(Condition condition) {
    List<Role> list=selectByCondition(condition);
    for(Role role:list)  {
      delete((Long)ColumnUtil.callPKGetMethod(role));
    }
  }

  public List<Role> selectByCondition(Condition condition) {
    String sql=SqlUtil.getSelectSql(Role.class, condition!=null?condition.generateCondition():null);
    List<Role> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, Role.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((Role)object);
    }
    return objs;
  }

  public PageInfo<Role> selectByConditionWithPagination(Condition condition,
      PageInfo<Role> pageInfo) {
    String sql=SqlUtil.getSelectSql(Role.class, condition!=null?condition.generateCondition():null);
    List<Role> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, Role.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((Role)object);
    }
     pageInfo.setPageRecordCount(objs.size());
    	  List<Role> temp=new ArrayList<>();
    			  int currentPage=pageInfo.getCurPage();
    			  int maxPageSize=pageInfo.getPageSize();
    			  int index=0;
    			  int start=(currentPage-1)*maxPageSize;
    			  for(Role role:objs) {
    				 if(index>=start&&index<start+maxPageSize) {
    					 temp.add(role);
    				 }
    				 index++;
    			  }
    	  pageInfo.setList(temp);
    	  return pageInfo;}

  @Deprecated
  public void insertBySql(String sql, Role role) {
    SqlUtil.save(sql, role);
  }

  @Deprecated
  public void deleteBySql(String sql_, Object[] params) {
    String[] arrStr=sql_.split("where");
    String sql=SqlUtil.getSelectSql(Role.class, null);
    sql+=" where "+arrStr[1];
    List<Object> list=SqlUtil.select(sql,Role.class, params);
    for(Object object:list) {
      SqlUtil.delete(SqlUtil.getDeleteSql(Role.class)+"where "+ClassUtil.getPrimaryKeyByClass(Role.class)+"=?", (Long)ColumnUtil.callPKGetMethod(object));
    }
  }

  @Deprecated
  public void updateBySql(String sql, Object[] params) {
    SqlUtil.modify(sql, params);
  }

  @Deprecated
  public List<Role> selectBySql(String sql, Object[] params) {
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

  public static class Condition {
    private StringBuffer sql = new StringBuffer();

    private int paramCount = 0;

    private List<Object> params = new ArrayList<>();

    private Map<Integer, String> cache = new HashMap();

    Condition(int paramCount, List<Object> params, StringBuffer sql) {
      this.paramCount = paramCount;
      this.params = params;
      this.sql = sql;
    }

    Condition() {
    }

    public Condition limit(long start, long end) {
      this.cache.put(3," limit " +start+" , "+end);
      return this;
    }

    public Condition OR() {
      this.sql.append(" OR ");
      return this;
    }

    public Condition orderBy(Object val) {
      this.cache.put(1," order By "+val+" ");
      return this;
    }

    /**
     * 第一个参数是参与排序字段名称(String)，第二参数为false时为降序排序(boolean) */
    public Condition orderBy(Object val, Object flag) {
      this.cache.put(1," order By "+val+" ");
      if(!(Boolean)flag) {
        DESC();
      }
      return this;
    }

    public Condition includ(Condition condition) {
      this.paramCount+=condition.paramCount;
      this.sql.append("("+condition.sql+")");
      this.params.addAll(condition.params);
      return this;
    }

    @Deprecated
    public Condition DESC() {
      this.cache.put(2," DESC ");
      return this;
    }

    private Condition simplify(String str, Object[] params) {
      this.paramCount++;
      if(params!=null) {
        for(Object param:params)  {
          this.params.add(param);
        }
      }
      if(paramCount>1&&!StringUtil.isLastKeywordEqualToOR(sql)) {
        this.sql.append(" and "+str);
      } else {
        this.sql.append(str);
        return this;
      }
      return this;
    }

    public String getSqlWithOutWhere() {
      return sql.toString();
    }

    public String generateCondition() {
      if(paramCount==0) {
        return sql.toString();
      } else {
        return "where "+sql.toString()+StringUtil.parseCache(cache);
      }
    }

    public Object[] generateParams() {
      return params.toArray();
    }

    public Condition andSdDasdIsNull() {
      return simplify("sd_dasd"+" is null ",null);
    }

    public Condition andSdDasdNotNull() {
      return simplify("sd_dasd"+" is not null ",null);
    }

    public Condition andSdDasdEqualTo(Object val) {
      return simplify("sd_dasd"+" = ? ",new Object[]{val});
    }

    public Condition andSdDasdNotEqualTo(Object val) {
      return simplify("sd_dasd"+" != ? ",new Object[]{val});
    }

    public Condition andSdDasdGreaterThan(Object val) {
      return simplify("sd_dasd"+" > ? ", new Object[]{val});
    }

    public Condition andSdDasdGreaterThanOrEqualTo(Object val) {
      return simplify( "sd_dasd"+" >= ? ", new Object[]{val});
    }

    public Condition andSdDasdLessThan(Object val) {
      return simplify( "sd_dasd"+" < ? ", new Object[]{val});
    }

    public Condition andSdDasdLessThanOrEqualTo(Object val) {
       return simplify("sd_dasd"+" <= ? ", new Object[]{val});
    }

    public Condition andSdDasdLike(Object val) {
      return simplify( "sd_dasd"+" like ? ", new Object[]{val});
    }

    public Condition andSdDasdNotLike(Object val) {
      return simplify( "sd_dasd"+" not like ? ", new Object[]{val});
    }

    public Condition andSdDasdIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("sd_dasd"+" in ("+sb.toString()+")",null);
    }

    public Condition andSdDasdNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("sd_dasd"+" not in ("+sb.toString()+")",null);
    }

    public Condition andSdDasdBetweenTo(Object start, Object end) {
      return simplify("sd_dasd"+" between ? and ?",new Object[]{start,end});
    }

    public Condition andSdDasdNotBetweenTo(Object start, Object end) {
      return simplify("sd_dasd"+" not between ? and ?",new Object[]{start,end});
    }

    public Condition andNameIsNull() {
      return simplify("name"+" is null ",null);
    }

    public Condition andNameNotNull() {
      return simplify("name"+" is not null ",null);
    }

    public Condition andNameEqualTo(Object val) {
      return simplify("name"+" = ? ",new Object[]{val});
    }

    public Condition andNameNotEqualTo(Object val) {
      return simplify("name"+" != ? ",new Object[]{val});
    }

    public Condition andNameGreaterThan(Object val) {
      return simplify("name"+" > ? ", new Object[]{val});
    }

    public Condition andNameGreaterThanOrEqualTo(Object val) {
      return simplify( "name"+" >= ? ", new Object[]{val});
    }

    public Condition andNameLessThan(Object val) {
      return simplify( "name"+" < ? ", new Object[]{val});
    }

    public Condition andNameLessThanOrEqualTo(Object val) {
       return simplify("name"+" <= ? ", new Object[]{val});
    }

    public Condition andNameLike(Object val) {
      return simplify( "name"+" like ? ", new Object[]{val});
    }

    public Condition andNameNotLike(Object val) {
      return simplify( "name"+" not like ? ", new Object[]{val});
    }

    public Condition andNameIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("name"+" in ("+sb.toString()+")",null);
    }

    public Condition andNameNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("name"+" not in ("+sb.toString()+")",null);
    }

    public Condition andNameBetweenTo(Object start, Object end) {
      return simplify("name"+" between ? and ?",new Object[]{start,end});
    }

    public Condition andNameNotBetweenTo(Object start, Object end) {
      return simplify("name"+" not between ? and ?",new Object[]{start,end});
    }

    public Condition andIdIsNull() {
      return simplify("id"+" is null ",null);
    }

    public Condition andIdNotNull() {
      return simplify("id"+" is not null ",null);
    }

    public Condition andIdEqualTo(Object val) {
      return simplify("id"+" = ? ",new Object[]{val});
    }

    public Condition andIdNotEqualTo(Object val) {
      return simplify("id"+" != ? ",new Object[]{val});
    }

    public Condition andIdGreaterThan(Object val) {
      return simplify("id"+" > ? ", new Object[]{val});
    }

    public Condition andIdGreaterThanOrEqualTo(Object val) {
      return simplify( "id"+" >= ? ", new Object[]{val});
    }

    public Condition andIdLessThan(Object val) {
      return simplify( "id"+" < ? ", new Object[]{val});
    }

    public Condition andIdLessThanOrEqualTo(Object val) {
       return simplify("id"+" <= ? ", new Object[]{val});
    }

    public Condition andIdLike(Object val) {
      return simplify( "id"+" like ? ", new Object[]{val});
    }

    public Condition andIdNotLike(Object val) {
      return simplify( "id"+" not like ? ", new Object[]{val});
    }

    public Condition andIdIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("id"+" in ("+sb.toString()+")",null);
    }

    public Condition andIdNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("id"+" not in ("+sb.toString()+")",null);
    }

    public Condition andIdBetweenTo(Object start, Object end) {
      return simplify("id"+" between ? and ?",new Object[]{start,end});
    }

    public Condition andIdNotBetweenTo(Object start, Object end) {
      return simplify("id"+" not between ? and ?",new Object[]{start,end});
    }

    public Condition andDescIsNull() {
      return simplify("desc_"+" is null ",null);
    }

    public Condition andDescNotNull() {
      return simplify("desc_"+" is not null ",null);
    }

    public Condition andDescEqualTo(Object val) {
      return simplify("desc_"+" = ? ",new Object[]{val});
    }

    public Condition andDescNotEqualTo(Object val) {
      return simplify("desc_"+" != ? ",new Object[]{val});
    }

    public Condition andDescGreaterThan(Object val) {
      return simplify("desc_"+" > ? ", new Object[]{val});
    }

    public Condition andDescGreaterThanOrEqualTo(Object val) {
      return simplify( "desc_"+" >= ? ", new Object[]{val});
    }

    public Condition andDescLessThan(Object val) {
      return simplify( "desc_"+" < ? ", new Object[]{val});
    }

    public Condition andDescLessThanOrEqualTo(Object val) {
       return simplify("desc_"+" <= ? ", new Object[]{val});
    }

    public Condition andDescLike(Object val) {
      return simplify( "desc_"+" like ? ", new Object[]{val});
    }

    public Condition andDescNotLike(Object val) {
      return simplify( "desc_"+" not like ? ", new Object[]{val});
    }

    public Condition andDescIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("desc_"+" in ("+sb.toString()+")",null);
    }

    public Condition andDescNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify("desc_"+" not in ("+sb.toString()+")",null);
    }

    public Condition andDescBetweenTo(Object start, Object end) {
      return simplify("desc_"+" between ? and ?",new Object[]{start,end});
    }

    public Condition andDescNotBetweenTo(Object start, Object end) {
      return simplify("desc_"+" not between ? and ?",new Object[]{start,end});
    }
  }
}
