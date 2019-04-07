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

public class UserMapper {
  public static void enabletransaction() {
    DBUtil.enableTransaction();
  }

  public static void commit() {
    DBUtil.commitTransaction();
  }

  public void insert(User user) {
    String sql=SqlUtil.getInsertSql(user.getClass());
    SqlUtil.save(sql,user);
  }

  public void delete(Long primaryKey) {
    String sql=SqlUtil.getDeleteSql(User.class)+"where id=?";
    SqlUtil.delete(sql,primaryKey);
  }

  public void update(User user) {
    String sql=SqlUtil.getUpdateSql(user.getClass())+"where id=?";
    SqlUtil.modify(sql,CollectionUtil.sortByUpdate(user));
  }

  public User select(Long primaryKey) {
    String sql=SqlUtil.getSelectSql(User.class, "where id=?");
    List<Object> temp=SqlUtil.select(sql,User.class,primaryKey);
    return temp!=null&&temp.size()!=0?(User)temp.get(0):null;
  }

  public void batchInsert(List<User> users) {
    for(User user:users) {
      insert(user);
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

  public void batchUpdate(List<User> users) {
    for(User user:users) {
      update(user);
    }
  }

  public List<User> batchSelect(Long[] primaryKeys) {
    List<User> users=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      users.add(select(primaryKey));
    }
    return users;
  }

  public List<User> batchSelect(List<Long> primaryKeys) {
    List<User> users=new ArrayList<>();
    for(Long primaryKey:primaryKeys) {
      users.add(select(primaryKey));
    }
    return users;
  }

  public void deleteByCondition(Condition condition) {
    List<User> list=selectByCondition(condition);
    for(User user:list)  {
      delete(user.getId());
    }
  }

  public List<User> selectByCondition(Condition condition) {
    String sql=SqlUtil.getSelectSql(User.class, condition!=null?condition.generateCondition():null);
    List<User> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, User.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((User)object);
    }
    return objs;
  }

  @Deprecated
  public void insertBySql(String sql, User user) {
    SqlUtil.save(sql, user);
  }

  @Deprecated
  public void deleteBySql(String sql_, Object[] params) {
    String[] arrStr=sql_.split("where");
    String sql=SqlUtil.getSelectSql(User.class, null);
    sql+=" where "+arrStr[1];
    List<Object> list=SqlUtil.select(sql,User.class, params);
    for(Object object:list) {
      SqlUtil.delete(SqlUtil.getDeleteSql(User.class)+"where id = ?", ((User)object).getId());
    }
  }

  @Deprecated
  public void updateBySql(String sql, Object[] params) {
    SqlUtil.modify(sql, params);
  }

  @Deprecated
  public List<User> selectBySql(String sql, Object[] params) {
    List<User> list=new ArrayList<>();
    List<Object> temps=SqlUtil.select(sql, User.class, params);
    for(Object temp:temps) {
      list.add((User)temp);
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

    public Condition andPasswordIsNull() {
      return simplify(" password is null ",null);
    }

    public Condition andPasswordNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andPasswordEqualTo(Object val) {
      return simplify(" password = ? ",new Object[]{val});
    }

    public Condition andPasswordNotEqualTo(Object val) {
      return simplify(" password != ? ",new Object[]{val});
    }

    public Condition andPasswordGreaterThan(Object val) {
      return simplify(" password > ? ", new Object[]{val});
    }

    public Condition andPasswordGreaterThanOrEqualTo(Object val) {
      return simplify(" password >= ? ", new Object[]{val});
    }

    public Condition andPasswordLessThan(Object val) {
      return simplify(" password < ? ", new Object[]{val});
    }

    public Condition andPasswordLessThanOrEqualTo(Object val) {
       return simplify(" password <= ? ", new Object[]{val});
    }

    public Condition andPasswordLike(Object val) {
      return simplify(" password like ? ", new Object[]{val});
    }

    public Condition andPasswordNotLike(Object val) {
      return simplify(" password not like ? ", new Object[]{val});
    }

    public Condition andPasswordIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" password in ("+sb.toString()+")",null);
    }

    public Condition andPasswordNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" password not in ("+sb.toString()+")",null);
    }

    public Condition andPasswordBetweenTo(Object start, Object end) {
      return simplify(" password between ? and ?",new Object[]{start,end});
    }

    public Condition andPasswordNotBetweenTo(Object start, Object end) {
      return simplify(" password not between ? and ?",new Object[]{start,end});
    }

    public Condition andSaltIsNull() {
      return simplify(" salt is null ",null);
    }

    public Condition andSaltNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andSaltEqualTo(Object val) {
      return simplify(" salt = ? ",new Object[]{val});
    }

    public Condition andSaltNotEqualTo(Object val) {
      return simplify(" salt != ? ",new Object[]{val});
    }

    public Condition andSaltGreaterThan(Object val) {
      return simplify(" salt > ? ", new Object[]{val});
    }

    public Condition andSaltGreaterThanOrEqualTo(Object val) {
      return simplify(" salt >= ? ", new Object[]{val});
    }

    public Condition andSaltLessThan(Object val) {
      return simplify(" salt < ? ", new Object[]{val});
    }

    public Condition andSaltLessThanOrEqualTo(Object val) {
       return simplify(" salt <= ? ", new Object[]{val});
    }

    public Condition andSaltLike(Object val) {
      return simplify(" salt like ? ", new Object[]{val});
    }

    public Condition andSaltNotLike(Object val) {
      return simplify(" salt not like ? ", new Object[]{val});
    }

    public Condition andSaltIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" salt in ("+sb.toString()+")",null);
    }

    public Condition andSaltNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" salt not in ("+sb.toString()+")",null);
    }

    public Condition andSaltBetweenTo(Object start, Object end) {
      return simplify(" salt between ? and ?",new Object[]{start,end});
    }

    public Condition andSaltNotBetweenTo(Object start, Object end) {
      return simplify(" salt not between ? and ?",new Object[]{start,end});
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
  }
}
