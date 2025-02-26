package com.ceiba.biblioteca.utilitarios.utilitarios.log;


import com.ceiba.biblioteca.utilitarios.utilitarios.DateHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.FileHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.Serializable;

public class ConectorLogger implements Serializable {

	private static final long serialVersionUID = 2134173316472428412L;
	private static Log log = LogFactory.getLog(ConectorLogger.class);
	
	
	public static void escribirRequerimientoSFClienteALog(String rutaLog, String prefijo, String tipoLog, String servicio, String pais, String xml){
		String reg = TemplateMessageHelper.getLogClientReqSFTemplate(DateHelper.getDateLogger(), servicio, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
	
	public static void escribirRequerimientoRRClienteALog(String rutaLog, String prefijo, String tipoLog, String servicio, String pais, String xml){
		String reg = TemplateMessageHelper.getLogClientReqRRTemplate(DateHelper.getDateLogger(), servicio, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
	
	public static void escribirRespuestaRRClienteALog(String rutaLog, String prefijo, String tipoLog, String servicio, String pais, String xml){
		String reg = TemplateMessageHelper.getLogClientRespRRTemplate(DateHelper.getDateLogger(), servicio, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
	
	public static void escribirRequerimientoRRServidorALog(String rutaLog, String prefijo, String tipoLog, String sourceQueue, String messageId, String correlationId, String messageType,String pais,  String xml){
		String reg = TemplateMessageHelper.getLogServerReqRRTemplate(DateHelper.getDateLogger(), sourceQueue, messageId, correlationId, messageType, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
	
	public static void escribirRequerimientoSFServidorALog(String rutaLog, String prefijo, String tipoLog, String sourceQueue, String messageId, String correlationId, String messageType,String pais,  String xml){
		String reg = TemplateMessageHelper.getLogServerReqSFTemplate(DateHelper.getDateLogger(), sourceQueue, messageId, correlationId, messageType, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
	
	public static void escribirRespuestaRRServidorALog(String rutaLog, String prefijo, String tipoLog, String sourceQueue, String messageId, String correlationId, String messageType, String pais, String xml){
		String reg = TemplateMessageHelper.getLogServerRespRRTemplate(DateHelper.getDateLogger(), sourceQueue, messageId, correlationId, messageType, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
		
	public static void escribirMQErrorLog(String rutaLog, String prefijo, String tipoLog, String servicio, String reasonCode, String symbol, String completitionCode, String pais, String xml, Throwable e){
		String reg = TemplateMessageHelper.getLogMQErrorTemplante(DateHelper.getDateLogger(), servicio, reasonCode, symbol, completitionCode, e, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
	
	public static void escribirBackendErrorLog(String rutaLog, String prefijo, String tipoLog, String pais, String sourceQueue, String messageId, String correlationId, String messageType, String xml){
		String reg = TemplateMessageHelper.getLogBackendErrorTemplante(DateHelper.getDateLogger(), sourceQueue, messageId, correlationId, messageType, xml);
		escribirArchivo(rutaLog, pais, prefijo, tipoLog, reg);
	}
	
	public static String getNombreArchivoLog(String rutaLog, String pais, String prefijo, String tipoLog){
		return rutaLog + File.separator + TemplateMessageHelper.getNombreArchivoLog(prefijo, tipoLog, pais, DateHelper.getDateLogger(DateHelper.LOG_NAME_DATE_FORMAT));
	}
	
	public static void escribirArchivo(String rutaLog, String pais, String prefijo, String tipoLog, String reg){
		try{
			String rutaArchivo = getNombreArchivoLog(rutaLog, pais, prefijo, tipoLog);
			FileHelper.crearArchivo(rutaArchivo, false);
			FileHelper.escribirArchivo(rutaArchivo, reg);
			//TODO al migrar a java 1.5+ favor modificar esto!!! o mejor a�n, dejar de usarlo que lo haga automaticamente el que guarda el archivo sobre el objeto File
			FileHelper.setReadWriteProperties(rutaArchivo);
		}
		catch (Exception e) {
			log.error("No se pudo aniadir informacion al log, favor verificar permisos de lectura/escritura",e);
		}
	}

}
