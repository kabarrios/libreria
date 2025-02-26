package com.ceiba.biblioteca.conectorCliente.MQ.principal;

import java.io.Serializable;

import com.ceiba.biblioteca.conectorCliente.MQ.cliente.EnviarMensajeMQI;
import com.ceiba.biblioteca.conectorCliente.MQ.cliente.MensajeAsincrono;
import com.ceiba.biblioteca.conectorCliente.MQ.cliente.MensajeSincrono;
import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.ThrowableHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.contingencias.Alarma;
import com.ceiba.biblioteca.utilitarios.utilitarios.log.ConectorLogger;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.MQReasonCodeResolver;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.QueueManagerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.mq.MQException;



import com.siman.Integracion.utilitarios.xml.SimpleXMLHelper;

/**
 * Clase que permite la inteacci�n con Websphere MQ a travez de MQI.
 * <p>Provee de m�todos para el envio de mensajes s�ncronos y as�ncronos.</p>
 * <p>Su forma de uso b�sico para mensajes as�ncronos es:</p>
 * <pre>
 * ConectorClienteBase conector = null;
 * int resultado = 0;
 * try{
 * 	conector = new ConectorCliente();
 *	resultado = conector.envioMensaje("CODIGO_SERVICIO", 
 *		"01", "01", "06", "01",
		"12051999", "192.168.133.100", 
		"4", "<datos_negocio/>", 
		ConstantesConector.NO_WAIT_TIME, "APPLICACION");
	if(resultado == 0){
		System.out.println("Exito en la transacci�n");
	}
	else{
		System.out.println("Error en la transacci�n");
	}
 * }
 * catch(Exception e){
 * 	e.printStackTrace();
 * }
 * finally{
 *  if(conector!=null){
 *   conector.closeQManagerQuietly();
 *  }
 * }
 * </pre>
 * <p>Su forma de uso b�sico para mensajes s�ncronos es:</p>
 * <pre>
 * ConectorClienteBase conector = null;
 * int resultado = 0;
 * try{
 * 	conector = new ConectorCliente();
 *	resultado = conector.envioMensaje("CODIGO_SERVICIO", 
 *		"01", "01", "06", "01",
		"12051999", "192.168.133.100", 
		"4", "<datos_negocio/>", 
		60, "APPLICACION"); //se espera 60 segundos
	if(resultado == 0){
		System.out.println("Exito en la transacci�n");
		conector.getBufferRespuesta();
	}
	else{
		System.out.println("Error en la transacci�n");
	}
 * }
 * catch(Exception e){
 * 	e.printStackTrace();
 * }
 * finally{
 *  if(conector!=null){
 *   conector.closeQManagerQuietly();
 *  }
 * }
 * </pre>
 * <p>La forma simplificada de uso es la siguiente: </p>
 * S�ncrono:<pre>ConectorCliente.sendSynchronousMessageToESB("CODIGO_SERVICIO", 
 *		"01", "01", "06", "01",
		"12051999", "192.168.133.100", 
		"4", "<datos_negocio/>", 
		60, "APPLICACION");</pre>
 * As�ncrono:<pre>ConectorCliente.sendAsynchronousMessageToESB("CODIGO_SERVICIO", 
 *		"01", "01", "06", "01",
		"12051999", "192.168.133.100", 
		"4", "<datos_negocio/>", 
		"APPLICACION");</pre>
 * @version 2.0
 * @author echicas
 * @see
 */
public class ConectorClienteMQI extends ConectorClienteBase implements Serializable {

	private static final long serialVersionUID = 4505965173624304346L;
	private static Log log = LogFactory.getLog(ConectorCliente.class);
	
	private QueueManagerContext context;
	/** variable que contiene las definiciones bases para envio de mensajes, se instancia de acuerdo a las necesidades */
	private EnviarMensajeMQI enviarMensaje = null;

	/**
	 * Inicia toda la configuraci�n necesaria para la comunicaci�n con Websphere MQ mediante MQI.
	 * Inicia la conexi�n de manera implicita, una vez instanciada la clase se debe cerrar la conexi�n cuando ya no se utilice.
	 * @see #closeQManager()
	 * @see #closeQManagerQuietly()
	 * @since 1.0
	 * @throws Exception Si se produce error al cargar los parametros para la conexi�n con Websphere MQ o si 
	 * existe un error al momento de realizar la conexi�n
	 */
	public ConectorClienteMQI() throws Exception {
		try {
			
			context = new QueueManagerContext();
			
			initParameters();
			
			//iniciamos al conexion con mq
			context.initConnection(configuracionMQ.getQueueManager(), configuracionMQ.getChannel(), configuracionMQ.getHost(), configuracionMQ.getPuertoServidor());
			
		} catch (MQException e) {
			String error = "Error al iniciar el contexto del conector cliente, no se puede continuar.\n" + TemplateMessageHelper.getMQError(e.reasonCode, e.completionCode, e);
			log.fatal(error, e);
			throw new Exception(error,e);
		}
	}
	
	public void closeQManagerQuietly(){
		this.context.closeQManagerQuietly();
	}
	
	public void closeQManager(){
		this.context.closeQManager();
	}
	
	/**
	 * M�todo que envia mensajes as�ncronos a Websphere MQ dado el contexto iniciado en el construct
	 * @param datosNegocio Datos que pertenecen al usuario (cliente) y que son los que corresponden a la transacci�n de negocio
	 * @return <code>0</code> si la transacci�n se completo con exito, <code>-1<code> en caso de un error no generado por Websphere MQ 
	 * y cualquier codigo de error correspondiente a Webpshere MQ. 
	 */
	protected int enviarMensajeAsincrono(String datosNegocio){
		int resultado = 0;
		enviarMensaje = new MensajeAsincrono(context.getQueueManager(), this.configuracionMQ, this.parametrosCliente, datosNegocio);
		try{
			escribirRequerimientoALog(enviarMensaje.getXMLDatosGCSSIEnvelope(), false);
			resultado = enviarMensaje.enviarMensaje(iAleatorio, this.parametrosCliente.getPrioridad());
			log.debug("Transaccion completada con exito a " + parametrosCliente.getCodigoServicio() + "." + configuracionMQ.getDefinicionRequerimiento());
		}
		catch (MQException e) {
			enviarMensajeAdministrarException(datosNegocio, e, e.reasonCode);
			return e.reasonCode;
		}
		catch (Throwable e) {
			enviarMensajeAdministrarException(datosNegocio, e, -1);
			return -1;
		}
		return resultado;
	}
	
	/**
	 * M�todo que envia mensajes s�ncronos a Websphere MQ dado el contexto iniciado en el constructo
	 * @param datosNegocio Datos que pertenecen al usuario (cliente) y que son los que corresponden a la transacci�n de negocio
	 * @return <code>0</code> si la transacci�n se completo con exito, <code>-1<code> en caso de un error no generado por Websphere MQ 
	 * y cualquier codigo de error correspondiente a Webpshere MQ. 
	 */
	public int enviarMensajeSincrono(String datosNegocio){
		enviarMensaje = new MensajeSincrono(context.getQueueManager(), this.configuracionMQ, this.parametrosCliente, datosNegocio);
		int resultado = 0;
		try{
			escribirRequerimientoALog(enviarMensaje.getXMLDatosGCSSIEnvelope(), true);
			resultado = enviarMensaje.enviarMensaje(iAleatorio, this.parametrosCliente.getPrioridad());
			this.bufferRespuesta = enviarMensaje.getDatosRespuesta();
			log.debug("Transaccion completada con exito a " + parametrosCliente.getCodigoServicio() + "." + configuracionMQ.getDefinicionRequerimiento());
			escribirRespuestaALog(this.bufferRespuesta);
		}
		catch (MQException e) {
			enviarMensajeAdministrarException(datosNegocio, e, e.reasonCode);
			return e.reasonCode;
		}
		catch (Throwable e) {
			enviarMensajeAdministrarException(datosNegocio, e, -1);
			return -1;
		}
		return resultado;
	}
	
	/**
	 * Envia a alarma el error y evalua el tipo de Excepci�n/Error para determinar el tipo de plantilla para log se utilizar�.
	 * En los logs de error/envio a Alarma no se discrima servicio, se envian todos sin excepci�n
	 * @param datosNegocio Datos a escribir en el log
	 * @param e para determinar el tipo de excepci�n y plantilla de log a utilizar
	 * @param reason c�digo que identifica el tipo de error (Websphere MQ u otro) para ser utilizado en el envio a alarma
	 */
	protected void enviarMensajeAdministrarException(String datosNegocio, Throwable e, int reason) {
		if (e instanceof MQException) {
			MQException ex = (MQException) e;
			log.error(TemplateMessageHelper.getMQError(ex.reasonCode, ex.completionCode, ex));
			ConectorLogger.escribirMQErrorLog(
					this.configuracionMQ.getRutaLog(), 
					this.parametrosCliente.getNombreAplicacionOrigen(), 
					this.configuracionMQ.getErrorTransaccionLogger(), 
					this.parametrosCliente.getCodigoServicio(), 
					Integer.toString(ex.reasonCode),
					MQReasonCodeResolver.resolve(ex),
					Integer.toString(ex.completionCode),
					this.parametrosCliente.getPaisOrigen(),
					datosNegocio,
					ex);
			Alarma.enviarAColaAlarma(context.getQueueManager(), configuracionMQ.getAlarma(), SimpleXMLHelper.getValueTag(ConstantesConector.HEADER_TAG_NAME, enviarMensaje.getXMLDatosGCSSIEnvelope()), Integer.toString(reason), TemplateMessageHelper.getMQError(ex.reasonCode, ex.completionCode, ex), datosNegocio);
		}
		else{
			ConectorLogger.escribirBackendErrorLog(
					this.configuracionMQ.getRutaLog(), 
					this.parametrosCliente.getNombreAplicacionOrigen(), 
					this.parametrosCliente.getNombreAplicacionOrigen(), 
					this.parametrosCliente.getPaisOrigen(), 
					"", 
					"", 
					"", 
					"", 
					datosNegocio);
			Alarma.enviarAColaAlarma(context.getQueueManager(), configuracionMQ.getAlarma(), SimpleXMLHelper.getValueTag(ConstantesConector.HEADER_TAG_NAME, enviarMensaje.getXMLDatosGCSSIEnvelope()), Integer.toString(reason), ThrowableHelper.stackTrace2String(e), datosNegocio);
		}
		log.error("Error al procesar la transaccion",e);
	}

	/**
	 * Envia un mensaje en modalidad asincrona a ESB, encapsula la funcionalidad extendia del conector cliente.
	 * @param servicio Codigo de serivico a ser utilizado
	 * @param pais Pais desde donde se genera la transaccion
	 * @param compania Compania para la que se genera la transaccion
	 * @param cadena Cadena para la que se genera la transaccion
	 * @param sucursal Sucursal para la que se genera la transaccion
	 * @param numeroTransaccion Numero de transaccion (campo solo descriptivo)
	 * @param hostOrigen IP/Nombre del servidor desde el que se genera la transaccion
	 * @param token Token (campo solo descriptivo)
	 * @param datos Datos de negocio
	 * @param nombreAplicacion Nombre del modulo/aplicacion que genera la transaccion
	 * @throws Exception Error generado durante el envio de mensaje y/o falta de recursos referenciados
	 */
	public static void sendAsynchronousMessageToESB(String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, String nombreAplicacion) throws Exception {
		int reason = 0;
		ConectorClienteBase conector = null;
		try {
			conector = new ConectorCliente();
			reason = conector.envioMensaje(servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, ConstantesConector.NO_WAIT_TIME, nombreAplicacion);
			if (reason != 0){
				throw new Exception("Error durante el envio del mensaje: " + reason);
			}
		} catch (Exception e) {
			throw new Exception("Error producido: " + reason + " " + e.getMessage(), e);
		} finally {
			if (conector != null)
				conector.closeQManagerQuietly();
		}
	}
	
	/**
	 * Envia un mensaje en modalidad sincrona a ESB, encapsula la funcionalidad extendia del conector cliente.
	 * @param servicio Codigo de serivico a ser utilizado
	 * @param pais Pais desde donde se genera la transaccion
	 * @param compania Compa�ia para la que se genera la transaccion
	 * @param cadena Cadena para la que se genera la transaccion
	 * @param sucursal Sucursal para la que se genera la transaccion
	 * @param numeroTransaccion Numero de transaccion (campo solo descriptivo)
	 * @param hostOrigen IP/Nombre del servidor desde el que se genera la transaccion
	 * @param token Token (campo solo descriptivo)
	 * @param datos Datos de negocio
	 * @param tiempoExpiracion Tiempo de espera por la respuesta a la peticion
	 * @param nombreAplicacion Nombre del modulo/aplicacion que genera la transaccion
	 * @return Datos obtenidos de la respuesta desde ESB
	 * @throws Exception Error generado durante el envio de mensaje y/o falta de recursos referenciados
	 */
	public static String sendSynchronousMessageToESB(String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, int tiempoExpiracion, String nombreAplicacion) throws Exception {
		int reason = 0;
		ConectorClienteBase conector = null;
		String response = null;
		try {
			conector = new ConectorCliente();
			reason = conector.envioMensaje(servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, tiempoExpiracion, nombreAplicacion);
			if (reason != 0){
				throw new Exception("Error durante el envio del mensaje: " + reason);
			}
			response = conector.getBufferRespuesta();
		} catch (Exception e) {
			throw new Exception("Error producido: " + reason + " " + e.getMessage(), e);
		} finally {
			if (conector != null)
				conector.closeQManagerQuietly();
		}
		return response;
	}


	/**
	 * Retorna la respuesta obtenida de la transacci�n sobre Websphere MQ.
	 * @return Respuesta del ESB a la solicitud asincrona de un servicio
	 */
	public String getBufferRespuesta() {
		return bufferRespuesta;
	}
	
	/**
	 * Retorna la respuesta obtenida de la transacci�n sobre Websphere MQ.
	 * @deprecated
	 * @see #getBufferRespuesta()
	 * @return Respuesta del ESB a la solicitud asincrona de un servicio 
	 */
	public String getRespBuffer(){
		return bufferRespuesta;
	}
	
	/**
	 * Retorna el objeto que contiene todo el contexto de conexi�n hacia Websphere MQ a trav�s de MQI
	 * @return instacia actual de {@link QueueManagerContext} 
	 */
	public QueueManagerContext getContext() {
		return context;
	}

}
