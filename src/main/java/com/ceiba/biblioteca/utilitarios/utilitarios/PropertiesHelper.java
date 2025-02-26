package com.ceiba.biblioteca.utilitarios.utilitarios;

import org.apache.commons.beanutils.BeanUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

//@SuppressWarnings("unchecked")
public class PropertiesHelper implements Serializable {

	private static final long serialVersionUID = -3582712778574148894L;

	public static Object properties2Bean(Properties properties, Class clazz){
		Object bean = null;
		try{
			Enumeration attr = properties.propertyNames();
			String name = null;
			bean = clazz.newInstance();
			Map map = new HashMap();
			while (attr.hasMoreElements()){
				name = (String) attr.nextElement();
				map.put(name, properties.getProperty(name));
			}
			BeanUtils.populate(bean, map);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		return bean;
	}
	
	public static Properties bean2Properties(Object object){
		Properties properties = null;
		try{
			Set keys = BeanUtils.describe(object).keySet();
			keys.remove("class");
			properties = new Properties();
			String key = null;
			for(Iterator it = keys.iterator(); it.hasNext();){
				key = (String)it.next();
				properties.put(key, BeanUtils.getProperty(object, key)==null?"null":BeanUtils.getProperty(object, key));
			}
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		return properties;
	}
	
	
	public static Properties getProperties(String propertiesFile) throws IOException{
		Properties properties = new Properties();
		properties.load(PropertiesHelper.class.getClassLoader().getResourceAsStream(propertiesFile));
		return properties;
	}
	
	public static Properties getPropertiesFromFileInputStream(String propertiesFile) throws IOException{
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		return properties;
	}
}
