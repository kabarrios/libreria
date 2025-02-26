package com.ceiba.biblioteca.utilitarios.utilitarios.mq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.Field;

public class MQReasonCodeResolver implements Serializable {

	private static final long serialVersionUID = -6407614259532716729L;
	private static final Log log = LogFactory.getLog(MQReasonCodeResolver.class);
	private static final String UNKNOWN = "unknown";
	public static final String MQ_EXCEPTION_CLASS = "com.ibm.mq.MQException";
	public static final String RC_FIELD_PREFIX = "MQRC_";
	public static final String RC_VALUE_FIELD = "reasonCode";

	/**
	 * Resolve reson code from MQException
	 * @param exception MQException to resolve
	 * @return symbolic name for reason code from MQException
	 * @see <code>com.ibm.mq.constants.MQConstants<code>
	 */
	//@Deprecated
	public static String resolve(Exception exception) {
		Class c = exception.getClass();
		try {
			Field f = c.getField(RC_VALUE_FIELD);
			Object key = f.get(exception);
			Field[] fields = c.getFields();
			String name = null;
			for (int i = 0; i < fields.length; i++) {
				name = fields[i].getName();
				if (name.startsWith(RC_FIELD_PREFIX) && fields[i].get(null).equals(key)) {
					return name;
				}
			}
		} 
		catch (Exception e) {
			log.warn("Unable to resolve reason code; " + e, e);
		}
		return UNKNOWN;
	}

	/**
	 * Verify if reason code can be resolved
	 * @param exception	MQException to resolve
	 * @return symbolic name for reason code from MQException
	 */
	public static boolean canResolve(Exception exception) {
		Class clazz = exception.getClass();
		while (Exception.class.isAssignableFrom(clazz)) {
			if (MQ_EXCEPTION_CLASS.equals(clazz.getName())){
				return true;
			}
			clazz = clazz.getSuperclass();
		}
		return false;
	}

}
