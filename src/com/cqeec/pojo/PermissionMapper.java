package com.cqeec.pojo;

import com.cqeec.bean.PageInfo;
import com.cqeec.util.ClassUtil;
import com.cqeec.util.CollectionUtil;
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

public class PermissionMapper {
  public static void enabletransaction() {
    DBUtil.enableTransaction();
  }

  public static void commit() {
    DBUtil.commitTransaction();
  }

  public void insert(Permission permission) {
    String sql=SqlUtil.getInsertSql(permission.getClass());
    SqlUtil.save(sql,permission);
  }

  public void delete(Long primaryKey) {
    String sql=SqlUtil.getDeleteSql(Permission.class)+"where "+ClassUtil.getPrimaryKeyByClass(Permission.class)+"=?";
    SqlUtil.delete(sql,primaryKey);
  }

  public void update(Permission permission) {
    String sql=SqlUtil.getUpdateSql(permission.getClass())+"where "+ClassUtil.getPrimaryKeyByClass(Permission.class)+"=?";
    SqlUtil.modify(sql,CollectionUtil.sortByUpdate(permission));
  }

  public Permission select(Long primaryKey) {
    String sql=SqlUtil.getSelectSql(Permission.class, "where "+ClassUtil.getPrimaryKeyByClass(Permission.class)+"=?");
    List<Object> temp=SqlUtil.select(sql,Permission.class,primaryKey);
    return temp!=null&&temp.size()!=0?(Permission)temp.get(0):null;
  }

  public void batchInsert(List<Permission> permissions) {
    for(Permission permission:permissions) {
      insert(permission);
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

  public void batchUpdate(List<Permission> permissions) {
    for(Permission permission:permissions) {
      update(permission);
    }
  }

  public List<Permission> batchSelect(Long[] primaryKeys) {
    List<Permission> permissions=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      permissions.add(select(primaryKey));
    }
    return permissions;
  }

  public List<Permission> batchSelect(List<Long> primaryKeys) {
    List<Permission> permissions=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      permissions.add(select(primaryKey));
    }
    return permissions;
  }

  public void deleteByCondition(Condition condition) {
    List<Permission> list=selectByCondition(condition);
    for(Permission permission:list)  {
      delete(permission.getId());
    }
  }

  public List<Permission> selectByCondition(Condition condition) {
    String sql=SqlUtil.getSelectSql(Permission.class, condition!=null?condition.generateCondition():null);
    List<Permission> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, Permission.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((Permission)object);
    }
    return objs;
  }

  public PageInfo<Permission> selectByConditionWithPagination(Condition condition,
      PageInfo<Permission> pageInfo) {
    String sql=SqlUtil.getSelectSql(Permission.class, condition!=null?condition.generateCondition():null);
    List<Permission> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, Permission.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((Permission)object);
    }
     pageInfo.setPageRecordCount(objs.size());
    	  List<Permission> temp=new ArrayList<>();
    			  int currentPage=pageInfo.getCurPage();
    			  int maxPageSize=pageInfo.getPageSize();
    			  int index=0;
    			  int start=(currentPage-1)*maxPageSize;
    			  for(Permission permission:objs) {
    				 if(index>=start&&index<start+maxPageSize) {
    					 temp.add(permission);
    				 }
    				 index++;
    			  }
    	  pageInfo.setList(temp);
    	  return pageInfo;}

  @Deprecated
  public void insertBySql(String sql, Permission permission) {
    SqlUtil.save(sql, permission);
  }

  @Deprecated
  public void deleteBySql(String sql_, Object[] params) {
    String[] arrStr=sql_.split("where");
    String sql=SqlUtil.getSelectSql(Permission.class, null);
    sql+=" where "+arrStr[1];
    List<Object> list=SqlUtil.select(sql,Permission.class, params);
    for(Object object:list) {
      SqlUtil.delete(SqlUtil.getDeleteSql(Permission.class)+"where "+ClassUtil.getPrimaryKeyByClass(Permission.class)+"=?", ((Permission)object).getId());
    }
  }

  @Deprecated
  public void updateBySql(String sql, Object[] params) {
    SqlUtil.modify(sql, params);
  }

  @Deprecated
  public List<Permission> selectBySql(String sql, Object[] params) {
    List<Permission> list=new ArrayList<>();
    List<Object> temps=SqlUtil.select(sql, Permission.class, params);
    for(Object temp:temps) {
      list.add((Permission)temp);
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

    public Condition andNameIsNull() {
      return simplify(" name is null ",null);
    }

    public Condition andNameNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andNameEqualTo(Object val) {
      return simplify(" name = ? ",new Object[]{val});
    }

    public Condition andNameNotEqualTo(Object val) {
      return simplify(" name != ? ",new Object[]{val});
    }

    public Condition andNameGreaterThan(Object val) {
      return simplify(" name > ? ", new Object[]{val});
    }

    public Condition andNameGreaterThanOrEqualTo(Object val) {
      return simplify(" name >= ? ", new Object[]{val});
    }

    public Condition andNameLessThan(Object val) {
      return simplify(" name < ? ", new Object[]{val});
    }

    public Condition andNameLessThanOrEqualTo(Object val) {
       return simplify(" name <= ? ", new Object[]{val});
    }

    public Condition andNameLike(Object val) {
      return simplify(" name like ? ", new Object[]{val});
    }

    public Condition andNameNotLike(Object val) {
      return simplify(" name not like ? ", new Object[]{val});
    }

    public Condition andNameIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" name in ("+sb.toString()+")",null);
    }

    public Condition andNameNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" name not in ("+sb.toString()+")",null);
    }

    public Condition andNameBetweenTo(Object start, Object end) {
      return simplify(" name between ? and ?",new Object[]{start,end});
    }

    public Condition andNameNotBetweenTo(Object start, Object end) {
      return simplify(" name not between ? and ?",new Object[]{start,end});
    }

    public Condition andIdIsNull() {
      return simplify(" id is null ",null);
    }

    public Condition andIdNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andIdEqualTo(Object val) {
      return simplify(" id = ? ",new Object[]{val});
    }

    public Condition andIdNotEqualTo(Object val) {
      return simplify(" id != ? ",new Object[]{val});
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

    public Condition andIdIn(List<Object> list) {
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

    public Condition andIdBetweenTo(Object start, Object end) {
      return simplify(" id between ? and ?",new Object[]{start,end});
    }

    public Condition andIdNotBetweenTo(Object start, Object end) {
      return simplify(" id not between ? and ?",new Object[]{start,end});
    }

    public Condition andDescIsNull() {
      return simplify(" desc_ is null ",null);
    }

    public Condition andDescNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andDescEqualTo(Object val) {
      return simplify(" desc_ = ? ",new Object[]{val});
    }

    public Condition andDescNotEqualTo(Object val) {
      return simplify(" desc_ != ? ",new Object[]{val});
    }

    public Condition andDescGreaterThan(Object val) {
      return simplify(" desc_ > ? ", new Object[]{val});
    }

    public Condition andDescGreaterThanOrEqualTo(Object val) {
      return simplify(" desc_ >= ? ", new Object[]{val});
    }

    public Condition andDescLessThan(Object val) {
      return simplify(" desc_ < ? ", new Object[]{val});
    }

    public Condition andDescLessThanOrEqualTo(Object val) {
       return simplify(" desc_ <= ? ", new Object[]{val});
    }

    public Condition andDescLike(Object val) {
      return simplify(" desc_ like ? ", new Object[]{val});
    }

    public Condition andDescNotLike(Object val) {
      return simplify(" desc_ not like ? ", new Object[]{val});
    }

    public Condition andDescIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" desc_ in ("+sb.toString()+")",null);
    }

    public Condition andDescNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" desc_ not in ("+sb.toString()+")",null);
    }

    public Condition andDescBetweenTo(Object start, Object end) {
      return simplify(" desc_ between ? and ?",new Object[]{start,end});
    }

    public Condition andDescNotBetweenTo(Object start, Object end) {
      return simplify(" desc_ not between ? and ?",new Object[]{start,end});
    }

    public Condition andUrlIsNull() {
      return simplify(" url is null ",null);
    }

    public Condition andUrlNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andUrlEqualTo(Object val) {
      return simplify(" url = ? ",new Object[]{val});
    }

    public Condition andUrlNotEqualTo(Object val) {
      return simplify(" url != ? ",new Object[]{val});
    }

    public Condition andUrlGreaterThan(Object val) {
      return simplify(" url > ? ", new Object[]{val});
    }

    public Condition andUrlGreaterThanOrEqualTo(Object val) {
      return simplify(" url >= ? ", new Object[]{val});
    }

    public Condition andUrlLessThan(Object val) {
      return simplify(" url < ? ", new Object[]{val});
    }

    public Condition andUrlLessThanOrEqualTo(Object val) {
       return simplify(" url <= ? ", new Object[]{val});
    }

    public Condition andUrlLike(Object val) {
      return simplify(" url like ? ", new Object[]{val});
    }

    public Condition andUrlNotLike(Object val) {
      return simplify(" url not like ? ", new Object[]{val});
    }

    public Condition andUrlIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" url in ("+sb.toString()+")",null);
    }

    public Condition andUrlNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" url not in ("+sb.toString()+")",null);
    }

    public Condition andUrlBetweenTo(Object start, Object end) {
      return simplify(" url between ? and ?",new Object[]{start,end});
    }

    public Condition andUrlNotBetweenTo(Object start, Object end) {
      return simplify(" url not between ? and ?",new Object[]{start,end});
    }
  }
}
