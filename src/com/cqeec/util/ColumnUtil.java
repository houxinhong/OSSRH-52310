package com.cqeec.util;

import java.lang.reflect.Field;

import com.cqeec.annotation.Column;
import com.cqeec.annotation.Id;

public class ColumnUtil {

	public static String getColumnNameByField(Field field) {
		Id id=field.getDeclaredAnnotation(Id.class);
		if(id!=null) {
			return id.value(); 
		}
		
		Column column=field.getAnnotation(Column.class);
		if(column==null&&id==null) {
			String temp=field.getName();
			if(StringUtil.getFirstUpperLetterIndex(temp)!=null) {
				Integer index=StringUtil.getFirstUpperLetterIndex(temp);
				while(index!=null) {
					String first=temp.substring(0,index);
					String second=temp.substring(index);
					second="_"+StringUtil.firstLetterLower(second);
					temp=first+second;
					index=StringUtil.getFirstUpperLetterIndex(temp);
				}
			}
			return temp;
		}else {
			String columnName=column.value();
			return columnName;
		}
	};
	
}
