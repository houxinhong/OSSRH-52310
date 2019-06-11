package com.cqeec.pojo;

import com.cqeec.bean.PageInfo;
import com.cqeec.core.DBUtil;
import com.cqeec.core.SqlUtil;
import com.cqeec.util.core.ClassUtil;
import com.cqeec.util.core.ColumnUtil;
import com.cqeec.util.other.CollectionUtil;
import com.cqeec.util.other.StringUtil;
import java.lang.Deprecated;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMapper {
  public static void enabletransaction() {
    DBUtil.enableTransaction();
  }

  public static void commit() {
    DBUtil.commitTransaction();
  }

  public void insert(Test test) {
    String sql=SqlUtil.getInsertSql(test.getClass());
    SqlUtil.save(sql,test);
  }

  public void delete(String primaryKey) {
    String sql=SqlUtil.getDeleteSql(Test.class)+"where "+ClassUtil.getPrimaryKeyByClass(Test.class)+"=?";
    SqlUtil.delete(sql,primaryKey);
  }

  public void update(Test test) {
    String sql=SqlUtil.getUpdateSql(test.getClass())+"where "+ClassUtil.getPrimaryKeyByClass(Test.class)+"=?";
    SqlUtil.modify(sql,CollectionUtil.sortByUpdate(test));
  }

  public Test select(String primaryKey) {
    String sql=SqlUtil.getSelectSql(Test.class, "where "+ClassUtil.getPrimaryKeyByClass(Test.class)+"=?");
    List<Object> temp=SqlUtil.select(sql,Test.class,primaryKey);
    return temp!=null&&temp.size()!=0?(Test)temp.get(0):null;
  }

  public void batchInsert(List<Test> tests) {
    for(Test test:tests) {
      insert(test);
    }
  }

  public void batchDelete(String[] primaryKeys) {
    for(String primaryKey:primaryKeys) {
      delete(primaryKey);
    }
  }

  public void batchDelete(List<String> primaryKeys) {
    for(String primaryKey:primaryKeys) {
      delete(primaryKey);
    }
  }

  public void batchUpdate(List<Test> tests) {
    for(Test test:tests) {
      update(test);
    }
  }

  public List<Test> batchSelect(String[] primaryKeys) {
    List<Test> tests=new ArrayList<>();
    for(String primaryKey:primaryKeys) {
      tests.add(select(primaryKey));
    }
    return tests;
  }

  public List<Test> batchSelect(List<String> primaryKeys) {
    List<Test> tests=new ArrayList<>();
    for(String primaryKey:primaryKeys) {
      tests.add(select(primaryKey));
    }
    return tests;
  }

  public void deleteByCondition(Condition condition) {
    List<Test> list=selectByCondition(condition);
    for(Test test:list)  {
      delete((String)ColumnUtil.callPKGetMethod(test));
    }
  }

  public List<Test> selectByCondition(Condition condition) {
    String sql=SqlUtil.getSelectSql(Test.class, condition!=null?condition.generateCondition():null);
    List<Test> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, Test.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((Test)object);
    }
    return objs;
  }

  public PageInfo<Test> selectByConditionWithPagination(Condition condition,
      PageInfo<Test> pageInfo) {
    String sql=SqlUtil.getSelectSql(Test.class, condition!=null?condition.generateCondition():null);
    List<Test> objs=new ArrayList<>();
    List<Object> list=SqlUtil.select(sql, Test.class,condition!=null?condition.generateParams():null);
    for(Object object:list) {
      objs.add((Test)object);
    }
     pageInfo.setPageRecordCount(objs.size());
    	  List<Test> temp=new ArrayList<>();
    			  int currentPage=pageInfo.getCurPage();
    			  int maxPageSize=pageInfo.getPageSize();
    			  int index=0;
    			  int start=(currentPage-1)*maxPageSize;
    			  for(Test test:objs) {
    				 if(index>=start&&index<start+maxPageSize) {
    					 temp.add(test);
    				 }
    				 index++;
    			  }
    	  pageInfo.setList(temp);
    	  return pageInfo;}

  @Deprecated
  public void insertBySql(String sql, Test test) {
    SqlUtil.save(sql, test);
  }

  @Deprecated
  public void deleteBySql(String sql_, Object[] params) {
    String[] arrStr=sql_.split("where");
    String sql=SqlUtil.getSelectSql(Test.class, null);
    sql+=" where "+arrStr[1];
    List<Object> list=SqlUtil.select(sql,Test.class, params);
    for(Object object:list) {
      SqlUtil.delete(SqlUtil.getDeleteSql(Test.class)+"where "+ClassUtil.getPrimaryKeyByClass(Test.class)+"=?", (String)ColumnUtil.callPKGetMethod(object));
    }
  }

  @Deprecated
  public void updateBySql(String sql, Object[] params) {
    SqlUtil.modify(sql, params);
  }

  @Deprecated
  public List<Test> selectBySql(String sql, Object[] params) {
    List<Test> list=new ArrayList<>();
    List<Object> temps=SqlUtil.select(sql, Test.class, params);
    for(Object temp:temps) {
      list.add((Test)temp);
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
  }
}
