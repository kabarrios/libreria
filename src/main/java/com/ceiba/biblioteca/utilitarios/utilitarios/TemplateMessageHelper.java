package com.ceiba.biblioteca.utilitarios.utilitarios;

import com.ceiba.biblioteca.utilitarios.utilitarios.log.ConectorLoggerBean;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.MQReasonCodeResolver;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.TemplateMensageBean;
import com.ibm.mq.MQException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.text.MessageFormat;

public class TemplateMessageHelper implements Serializable {
	

	private static final long serialVersionUID = 2168583724951273766L;
	private static Log log = LogFactory.getLog(TemplateMessageHelper.class);
	private static ConectorLoggerBean configuracionLog;
	private static TemplateMensageBean plantillaMensajes;
	
	static{
		try{
			log.info("Configurando plantillas para logs y mensajes");
			configuracionLog = (ConectorLoggerBean)PropertiesHelper.properties2Bean(PropertiesHelper.getProperties(ConstantesConector.PLANTILLA_LOG_PROPERTIES), ConectorLoggerBean.class);
			plantillaMensajes = (TemplateMensageBean)PropertiesHelper.properties2Bean(PropertiesHelper.getProperties(ConstantesConector.PLANTILLA_MENSAJES_PROPERTIES), TemplateMensageBean.class);
			log.info("Finalizada la configuracion de plantillas para logs y mensajes");
		}
		catch (Exception e) {
			log.fatal(getSimpleMessage(ConstantesConector.MSG_ERROR_INIT_PLANTILLAS),e);
			throw new RuntimeException(getSimpleMessage(ConstantesConector.MSG_ERROR_INIT_PLANTILLAS),e);
		}
	}
	
	public static ConectorLoggerBean getConfiguracionLog() {
		return configuracionLog;
	}

	public static TemplateMensageBean getPlantillaMensajes() {
		return plantillaMensajes;
	}
	
	public static String getSimpleMessage(String key){
		return Messages.message(key);
	}
	
	public static String getSimpleMessage(String key, Object[] params){
		return Messages.format(key, params);
	}
	
	public static String getMQError(int reasonCode, int completitionCode, MQException e){
		return Messages.format(Integer.toString(reasonCode), new Object[]{Integer.toString(reasonCode), MQReasonCodeResolver.resolve(e), Integer.toString(completitionCode), ThrowableHelper.stackTrace2String(e)});
	}
	
	public static String getQueueManagerInfo(String key, String queueManagerName, String hostname, Integer port, String channel){
		return Messages.format(key,  new Object[]{queueManagerName, hostname, port, channel});
	}
	
	public static String getXmlRespuestaServidor(String header, String returnCode, String returnCodeDescription ,String datos){
		return MessageFormat.format(plantillaMensajes.getRespuestaServer(),  new Object[]{header, returnCode, returnCodeDescription, datos, ""});
	}
	
	/**
	 * Construye la respueta en formato xml del conector servidor incluyendo retcode y su respectiva descripci�n, ademas de la secci�n ServiceVars
	 * @param header 
	 * @param serviceVars
	 * @param returnCode
	 * @param returnCodeDescription
	 * @param datos
	 * @return
	 */
	public static String getXmlRespuestaServidor(String header, String serviceVars, String returnCode, String returnCodeDescription ,String datos){
		return MessageFormat.format(plantillaMensajes.getRespuestaServer(),  new Object[]{header, returnCode, returnCodeDescription, datos, serviceVars});
	}
	
	public static String getXmlRespuestaBackend(String returnCode, String returnCodeDescription ,String datos){
		return MessageFormat.format(plantillaMensajes.getRespuestaBackEnd(),  new Object[]{returnCode, returnCodeDescription, datos});
	}
	
	public static String getXmlRequerimientoServidor(String header, String datos){
		return MessageFormat.format(plantillaMensajes.getRequerimientoServer(),  new Object[]{header, datos});
	}
	
	public static String getXmlRequerimientoCliente(String servicio, String usuario, String clave, String nombreApplicacion, String pais, String compania, String cadena, String sucursal, String numtran, String hostName, String token, String version, String datos){
		return MessageFormat.format(plantillaMensajes.getRequerimientoCliente(),  new Object[]{servicio, usuario, clave, nombreApplicacion, pais, compania, cadena, sucursal, numtran, hostName, token, version, datos});
	}
	
	public static String getXmlRespuestaCliente(String servicio, String usuario, String clave, String nombreApplicacion, String pais, String compania, String cadena, String sucursal, String numtran, String hostName, String token, String version, String datos){
		throw new UnsupportedOperationException("Operacion no implementada");
	}
	
	public static String getLogClientReqRRTemplate(String fecha, String servicio, String datos){
		return MessageFormat.format(configuracionLog.getLogClientReqRRTemplate(),  new Object[]{fecha, servicio, datos});		
	}
	
	public static String getLogClientReqSFTemplate(String fecha, String servicio, String datos){
		return MessageFormat.format(configuracionLog.getLogClientReqSFTemplate(),  new Object[]{fecha, servicio, datos});		
	}
	
	public static String getLogClientRespRRTemplate(String fecha, String servicio, String datos){
		return MessageFormat.format(configuracionLog.getLogClientRespRRTemplate(),  new Object[]{fecha, servicio, datos});		
	}
	
	public static String getLogServerReqRRTemplate(String fecha, String sourceQueue, String messageId, String correlationId, String messageType, String datos){
		return MessageFormat.format(configuracionLog.getLogServerReqRRTemplate(),  new Object[]{fecha, sourceQueue, messageId, correlationId, messageType, datos});		
	}
	
	public static String getLogServerReqSFTemplate(String fecha, String sourceQueue, String messageId, String correlationId, String messageType, String datos){
		return MessageFormat.format(configuracionLog.getLogServerReqSFTemplate(),  new Object[]{fecha, sourceQueue, messageId, correlationId, messageType, datos});		
	}
	
	public static String getLogServerRespRRTemplate(String fecha, String sourceQueue, String messageId, String correlationId, String messageType, String datos){
		return MessageFormat.format(configuracionLog.getLogServerRespRRTemplate(),  new Object[]{fecha, sourceQueue, messageId, correlationId, messageType, datos});		
	}
	
	public static String getLogMQErrorTemplante(String fecha, String servicio, String reasonCode, String symbol, String completitionCode, Throwable e, String datos){
		return MessageFormat.format(configuracionLog.getLogMQErrorTemplante(), new Object[]{fecha, servicio, reasonCode, symbol, completitionCode, ThrowableHelper.stackTrace2String(e), datos});		
	}
	
	public static String getLogBackendErrorTemplante(String fecha, String sourceQueue, String messageId, String correlationId, String messageType, String datos){
		return MessageFormat.format(configuracionLog.getLogBackeneErrorTemplate(), new Object[]{fecha, sourceQueue, correlationId, messageId, messageType, datos});		
	}
	
	public static String getNombreArchivoLog(String prefijo, String tipoLog, String pais, String fecha){
		return MessageFormat.format(TemplateMessageHelper.getConfiguracionLog().getLogFileTemplate(), new Object[]{prefijo, tipoLog, pais, fecha});
	}
	
	public static String getXmlAlarma(String header, String codigoError, String descripcionError, String datosRequerimiento, String datosRespuesta){
		return MessageFormat.format(TemplateMessageHelper.getPlantillaMensajes().getAlarma(),  new Object[]{header, codigoError, descripcionError, datosRequerimiento, datosRespuesta});
	}

}
