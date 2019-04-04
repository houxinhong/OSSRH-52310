package test;

import org.junit.Test;

import com.cqeec.util.ClassUtil;
import com.cqeec.util.GenerateCodeUtil;

public class GenerateJavaFile {

	
	
	@Test
	public void test01(){
	}
	
	@Test
	public void test02() {
		GenerateCodeUtil.generateJavaFile("config.properties");
		GenerateCodeUtil.generateMapper("config.properties");
	}
	
	
	
	
}
