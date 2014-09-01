package cn.edu.buaa.act.SCAS.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class Test {
	
	public static void main(String[] args){
		
		BeanFactory factory = new XmlBeanFactory(new FileSystemResource("D:\\STS\\workspace\\SCASystem\\src\\main\\resources\\beans.xml"));  
        Car obj = (Car)factory.getBean("car");
        System.out.println(obj.getDicky());
	}
}
