package com.ceiba.biblioteca.utilitarios.utilitarios;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BeanHelper implements Serializable {

	private static final String STUPID_NULL = "null";
	private static final String EQUAL = "=";
	private static final String CLASS = "class";
	private static final long serialVersionUID = 6140293603547742861L;
	private static final String SEPARATOR = " ";

	public static String bean2String(Object o) {
		try {
			return bean2String(o, SEPARATOR);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//@SuppressWarnings("unchecked")
	public static String bean2String(Object o, String separator) {
		String bean = new String();
		try {
			StringBuffer result = new StringBuffer();
			Set keys = BeanUtils.describe(o).keySet();
			keys.remove(CLASS);
			String key = null;
			for (Iterator it = keys.iterator(); it.hasNext();) {
				key = (String) it.next();
				result.append(key);
				result.append(EQUAL);
				result.append(BeanUtils.getProperty(o, key));
				result.append(separator);
			}
			bean = result.subSequence(0, result.lastIndexOf(separator)).toString();
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bean;
	}

	//@SuppressWarnings("unchecked")
	public static void replaceStupidNullAttributes(Object bean) {
		try {
			String key = null;
			Map desc = BeanUtils.describe(bean);
			for (Iterator iterator = desc.keySet().iterator(); iterator.hasNext();) {
				key = (String) iterator.next();
				if (desc.get(key) != null) {
					if (((String) desc.get(key)).equalsIgnoreCase(STUPID_NULL)) {
						desc.put(key, null);
					}
				}
			}
			BeanUtils.populate(bean, desc);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
