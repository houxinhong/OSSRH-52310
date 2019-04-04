package test;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.cqeec.util.ClassUtil;
import com.cqeec.util.FileParseUtil;
import com.cqeec.util.StringUtil;
import com.squareup.javapoet.ClassName;

public class FileTest {

	
	
	@Test
	public void test01() {
		File file=new File("src/com/cqeec/pojo");
		File[] files=file.listFiles();
		for(File file2:files) {
		}
	}
	
	
	@Test
	public void test02() {
		
		List<Class> list=ClassUtil.getClassListByPackage("src/com/cqeec/pojo");
		System.out.println(list.size());
	}
	
	
	@Test
	public void test03() throws InstantiationException, IllegalAccessException {
		List<ClassName> list=ClassUtil.getClassNameList(FileParseUtil.parsePropertyFile("config.properties"));
		System.out.println(list.size());;
	}
	
	
	
}
