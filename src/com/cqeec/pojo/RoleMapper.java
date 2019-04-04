package com.cqeec.pojo;

import com.cqeec.util.CollectionUtil;
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

  public void batchDelete(List<Long> ids) {
    for(Long id:ids) {
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

  public List<Role> batchSelect(List<Long> ids) {
    List<Role> roles=new ArrayList<>();
    for(Long id:ids) {
      roles.add(select(id));
    }
    return roles;
  }

  public void deleteByCondition(Condition condition) {
    List<Role> list=selectByCondition(condition);
    for(Role role:list)  {
      delete(role.getId());
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
      SqlUtil.delete(SqlUtil.getDeleteSql(Role.class)+"where id = ?", ((Role)object).getId());
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

    public Condition andNameIsNull() {
      return simplify(" Name is null ",null);
    }

    public Condition andNameNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andNameEqualTo(Object val) {
      return simplify(" Name = ? ",new Object[]{val});
    }

    public Condition andNameNotEqualTo(Object val) {
      return simplify(" Name != ? ",new Object[]{val});
    }

    public Condition andNameGreaterThan(Object val) {
      return simplify(" Name > ? ", new Object[]{val});
    }

    public Condition andNameGreaterThanOrEqualTo(Object val) {
      return simplify(" Name >= ? ", new Object[]{val});
    }

    public Condition andNameLessThan(Object val) {
      return simplify(" Name < ? ", new Object[]{val});
    }

    public Condition andNameLessThanOrEqualTo(Object val) {
       return simplify(" Name <= ? ", new Object[]{val});
    }

    public Condition andNameLike(Object val) {
      return simplify(" Name like ? ", new Object[]{val});
    }

    public Condition andNameNotLike(Object val) {
      return simplify(" Name not like ? ", new Object[]{val});
    }

    public Condition andNameIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" Name in ("+sb.toString()+")",null);
    }

    public Condition andNameNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" Name not in ("+sb.toString()+")",null);
    }

    public Condition andNameBetweenTo(Object start, Object end) {
      return simplify(" Name between ? and ?",new Object[]{start,end});
    }

    public Condition andNameNotBetweenTo(Object start, Object end) {
      return simplify(" Name not between ? and ?",new Object[]{start,end});
    }

    public Condition andIdIsNull() {
      return simplify(" Id is null ",null);
    }

    public Condition andIdNotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andIdEqualTo(Object val) {
      return simplify(" Id = ? ",new Object[]{val});
    }

    public Condition andIdNotEqualTo(Object val) {
      return simplify(" Id != ? ",new Object[]{val});
    }

    public Condition andIdGreaterThan(Object val) {
      return simplify(" Id > ? ", new Object[]{val});
    }

    public Condition andIdGreaterThanOrEqualTo(Object val) {
      return simplify(" Id >= ? ", new Object[]{val});
    }

    public Condition andIdLessThan(Object val) {
      return simplify(" Id < ? ", new Object[]{val});
    }

    public Condition andIdLessThanOrEqualTo(Object val) {
       return simplify(" Id <= ? ", new Object[]{val});
    }

    public Condition andIdLike(Object val) {
      return simplify(" Id like ? ", new Object[]{val});
    }

    public Condition andIdNotLike(Object val) {
      return simplify(" Id not like ? ", new Object[]{val});
    }

    public Condition andIdIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" Id in ("+sb.toString()+")",null);
    }

    public Condition andIdNotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" Id not in ("+sb.toString()+")",null);
    }

    public Condition andIdBetweenTo(Object start, Object end) {
      return simplify(" Id between ? and ?",new Object[]{start,end});
    }

    public Condition andIdNotBetweenTo(Object start, Object end) {
      return simplify(" Id not between ? and ?",new Object[]{start,end});
    }

    public Condition andDesc_IsNull() {
      return simplify(" Desc_ is null ",null);
    }

    public Condition andDesc_NotNull() {
      return simplify(" id is not null ",null);
    }

    public Condition andDesc_EqualTo(Object val) {
      return simplify(" Desc_ = ? ",new Object[]{val});
    }

    public Condition andDesc_NotEqualTo(Object val) {
      return simplify(" Desc_ != ? ",new Object[]{val});
    }

    public Condition andDesc_GreaterThan(Object val) {
      return simplify(" Desc_ > ? ", new Object[]{val});
    }

    public Condition andDesc_GreaterThanOrEqualTo(Object val) {
      return simplify(" Desc_ >= ? ", new Object[]{val});
    }

    public Condition andDesc_LessThan(Object val) {
      return simplify(" Desc_ < ? ", new Object[]{val});
    }

    public Condition andDesc_LessThanOrEqualTo(Object val) {
       return simplify(" Desc_ <= ? ", new Object[]{val});
    }

    public Condition andDesc_Like(Object val) {
      return simplify(" Desc_ like ? ", new Object[]{val});
    }

    public Condition andDesc_NotLike(Object val) {
      return simplify(" Desc_ not like ? ", new Object[]{val});
    }

    public Condition andDesc_In(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" Desc_ in ("+sb.toString()+")",null);
    }

    public Condition andDesc_NotIn(List<Object> list) {
      this.params.addAll(list);
      StringBuffer sb=new StringBuffer();
      for(Object object:list) {
        sb.append("?,");
      }
      StringUtil.clearEndChar(sb);
      return simplify(" Desc_ not in ("+sb.toString()+")",null);
    }

    public Condition andDesc_BetweenTo(Object start, Object end) {
      return simplify(" Desc_ between ? and ?",new Object[]{start,end});
    }

    public Condition andDesc_NotBetweenTo(Object start, Object end) {
      return simplify(" Desc_ not between ? and ?",new Object[]{start,end});
    }

    public Condition limit(long start, long end) {
      this.cache.put(3," limit ?,? ");
      this.params.add(start);
      this.params.add(end);
      return this;
    }

    public Condition Or() {
      this.sql.append(" OR ");
      return this;
    }

    public Condition orderBy(Object val) {
      this.cache.put(1," order By "+val+" ");
      return this;
    }

    public Condition includ(Condition condition) {
      this.paramCount+=condition.paramCount;
      this.sql.append("("+condition.sql+")");
      this.params.addAll(condition.params);
      return this;
    }

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
      if(paramCount==1) {
        this.sql.append(str);
      } else {
        this.sql.append(" and "+str);
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
  }
}
