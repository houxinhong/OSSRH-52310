package com.cqeec.util;

import java.lang.reflect.Field;

import com.cqeec.annotation.Column;

public class ColumnUtil {

	public static String getColumnNameByField(Field field) {
		Column column=field.getAnnotation(Column.class);
		if(column==null) {
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
