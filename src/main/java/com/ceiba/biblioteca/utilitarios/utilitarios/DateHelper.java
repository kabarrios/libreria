package com.ceiba.biblioteca.utilitarios.utilitarios;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper implements Serializable {

	private static final long serialVersionUID = -3017231854922156296L;
	
	public static String CONNECTOR_DATE_FORMAT = "dd/MM/yyyy-HH:mm:ss.SSS";
	public static String LOG_NAME_DATE_FORMAT = "yyyyMMdd";
	
	public static String getDateLogger(){
		Format sdf = new SimpleDateFormat(CONNECTOR_DATE_FORMAT, Locale.ENGLISH);
		return sdf.format(new Date());
	}
	
	public static String getDateLogger(Locale locale){
		Format sdf = new SimpleDateFormat(CONNECTOR_DATE_FORMAT, locale);
		return sdf.format(new Date());
	}
		
	public static String getDateLogger(String format){
		Format sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	public static String getDateLogger(String format, Locale locale){
		Format sdf = new SimpleDateFormat(format, locale);
		return sdf.format(new Date());
	}

}
