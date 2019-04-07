package com.cqeec.pojo;

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

public class UserRoleMapper {
  public static void enabletransaction() {
    DBUtil.enableTransaction();
  }

  public static void commit() {
    DBUtil.commitTransaction();
  }

  public void insert(UserRole userRole) {
    String sql=SqlUtil.getInsertSql(userRole.getClass());
    SqlUtil.save(sql,userRole);
  }

  public void delete(Long primaryKey) {
    String sql=SqlUtil.getDeleteSql(UserRole.class)+"where id=?";
    SqlUtil.delete(sql,primaryKey);
  }

  public void update(UserRole userRole) {
    String sql=SqlUtil.getUpdateSql(userRole.getClass())+"where id=?";
    SqlUtil.modify(sql,CollectionUtil.sortByUpdate(userRole));
  }

  public UserRole select(Long primaryKey) {
    String sql=SqlUtil.getSelectSql(UserRole.class, "where id=?");
    List<Object> temp=SqlUtil.select(sql,UserRole.class,primaryKey);
    return temp!=null&&temp.size()!=0?(UserRole)temp.get(0):null;
  }

  public void batchInsert(List<UserRole> userRoles) {
    for(UserRole userRole:userRoles) {
      insert(userRole);
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

  public void batchUpdate(List<UserRole> userRoles) {
    for(UserRole userRole:userRoles) {
      update(userRole);
    }
  }

  public List<UserRole> batchSelect(Long[] primaryKeys) {
    List<UserRole> userRoles=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      userRoles.add(select(primaryKey));
    }
    return userRoles;
  }

  public List<UserRole> batchSelect(List<Long> primaryKeys) {
    List<UserRole> userRoles=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      userRoles.add(select(primaryKey));
    }
    return userRoles;
  }

  public void deleteByCondition(Condition condition) {
    List<UserRole> list=selectByCondition(condition);
    for(UserRole userRole:list)  {
      delete(userRole.getId());
    }
  }

  public List<UserRole> selectByCondition(Condition condition) {
    String sql=SqlUtil.getSelectSql(UserRole.class, condition!=null?condition.generateCondition():null);
    List<UserRole> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, UserRole.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((UserRole)object);
    }
    return objs;
  }

  @Deprecated
  public void insertBySql(String sql, UserRole userRole) {
    SqlUtil.save(sql, userRole);
  }

  @Deprecated
  public void deleteBySql(String sql_, Object[] params) {
    String[] arrStr=sql_.split("where");
    String sql=SqlUtil.getSelectSql(UserRole.class, null);
    sql+=" where "+arrStr[1];
    List<Object> list=SqlUtil.select(sql,UserRole.class, params);
    for(Object object:list) {
      SqlUtil.delete(SqlUtil.getDeleteSql(UserRole.class)+"where id = ?", ((UserRole)object).getId());
    }
  }

  @Deprecated
  public void updateBySql(String sql, Object[] params) {
    SqlUtil.modify(sql, params);
  }

  @Deprecated
  public List<UserRole> selectBySql(String sql, Object[] params) {
    List<UserRole> list=new ArrayList<>();
    List<Object> temps=SqlUtil.select(sql, UserRole.class, params);
    for(Object temp:temps) {
      list.add((UserRole)temp);
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

    public Condition andUidIsNull() {
      return simplify(" uid is null ",null);
    }

    public Condition andUidNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andUidEqualTo(Object val) {
      return simplify(" uid = ? ",new Object[]{val});
    }

    public Condition andUidNotEqualTo(Object val) {
      return simplify(" uid != ? ",new Object[]{val});
    }

    public Condition andUidGreaterThan(Object val) {
      return simplify(" uid > ? ", new Object[]{val});
    }

    public Condition andUidGreaterThanOrEqualTo(Object val) {
      return simplify(" uid >= ? ", new Object[]{val});
    }

    public Condition andUidLessThan(Object val) {
      return simplify(" uid < ? ", new Object[]{val});
    }

    public Condition andUidLessThanOrEqualTo(Object val) {
       return simplify(" uid <= ? ", new Object[]{val});
    }

    public Condition andUidLike(Object val) {
      return simplify(" uid like ? ", new Object[]{val});
    }

    public Condition andUidNotLike(Object val) {
      return simplify(" uid not like ? ", new Object[]{val});
    }

    public Condition andUidIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" uid in ("+sb.toString()+")",null);
    }

    public Condition andUidNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" uid not in ("+sb.toString()+")",null);
    }

    public Condition andUidBetweenTo(Object start, Object end) {
      return simplify(" uid between ? and ?",new Object[]{start,end});
    }

    public Condition andUidNotBetweenTo(Object start, Object end) {
      return simplify(" uid not between ? and ?",new Object[]{start,end});
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

    public Condition andRidIsNull() {
      return simplify(" rid is null ",null);
    }

    public Condition andRidNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andRidEqualTo(Object val) {
      return simplify(" rid = ? ",new Object[]{val});
    }

    public Condition andRidNotEqualTo(Object val) {
      return simplify(" rid != ? ",new Object[]{val});
    }

    public Condition andRidGreaterThan(Object val) {
      return simplify(" rid > ? ", new Object[]{val});
    }

    public Condition andRidGreaterThanOrEqualTo(Object val) {
      return simplify(" rid >= ? ", new Object[]{val});
    }

    public Condition andRidLessThan(Object val) {
      return simplify(" rid < ? ", new Object[]{val});
    }

    public Condition andRidLessThanOrEqualTo(Object val) {
       return simplify(" rid <= ? ", new Object[]{val});
    }

    public Condition andRidLike(Object val) {
      return simplify(" rid like ? ", new Object[]{val});
    }

    public Condition andRidNotLike(Object val) {
      return simplify(" rid not like ? ", new Object[]{val});
    }

    public Condition andRidIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" rid in ("+sb.toString()+")",null);
    }

    public Condition andRidNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" rid not in ("+sb.toString()+")",null);
    }

    public Condition andRidBetweenTo(Object start, Object end) {
      return simplify(" rid between ? and ?",new Object[]{start,end});
    }

    public Condition andRidNotBetweenTo(Object start, Object end) {
      return simplify(" rid not between ? and ?",new Object[]{start,end});
    }
  }
}
