package com.cqeec.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileParseUtil {

	public static Properties parsePropertyFile(String path){
		Properties properties=new Properties();
        
		InputStream is=FileParseUtil.class.getClassLoader().getResourceAsStream(path);
		try {
			properties.load(is);
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
