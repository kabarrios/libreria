package com.ceiba.biblioteca.conectorCliente.MQ.principal;

import com.ceiba.biblioteca.conectorCliente.MQ.cliente.EnviarMensajeJMS;
import com.ceiba.biblioteca.conectorCliente.MQ.cliente.MensajeAsincronoJMS;
import com.ceiba.biblioteca.conectorCliente.MQ.cliente.MensajeSincronoJMS;
import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.TemplateMessageHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.ThrowableHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.contingencias.AlarmaJMS;
import com.ceiba.biblioteca.utilitarios.utilitarios.log.ConectorLogger;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.MQReasonCodeResolver;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms.JMSQueueManagerContext;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms.JMSQueueManagerContextNojndi;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms.JMSQueueManagerContextjndi;
import com.ibm.mq.MQException;
import com.ibm.msg.client.jms.DetailedMessageFormatException;
import com.siman.Integracion.utilitarios.xml.SimpleXMLHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConectorClienteJMS extends ConectorClienteBase implements Serializable {
	
	private static final long serialVersionUID = 8748401422177189432L;
	private static Log log = LogFactory.getLog(ConectorClienteJMS.class);
	
	/**
	 * Contiene el contexto de conexi�n, ya sea con jndi o sin jndi
	 */
	private JMSQueueManagerContext context;
	/**
	 * Variable para almacenar la implementaci�n de mensajeria s�ncrona o as�ncrona
	 */
	private EnviarMensajeJMS enviarMensaje;
	
	/**
	 * Inicia toda la configuraci�n necesaria para la comunicaci�n con Websphere MQ mediante JMS.
	 * @param jndiResource Nombre del recurso JNDI que contiene el {@link QueueConnectionFactory}
	 * @see #closeQManager()
	 * @see #closeQManagerQuietly()
	 * @since 1.0
	 * @throws Exception Si se produce error al cargar los parametros para la conexi�n con Websphere MQ
	 */
	public ConectorClienteJMS(String jndiResource){
		try{
			this.initParameters();
			this.context = new JMSQueueManagerContextjndi();
			this.context.initConnection(jndiResource);
		}
		catch (Throwable e) {
			log.fatal("Error al iniciar el contexto de conexi�n", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Inicia toda la configuraci�n necesaria para la comunicaci�n con Websphere MQ mediante JMS.
	 * @see #closeQManager()
	 * @see #closeQManagerQuietly()
	 * @since 1.0
	 * @throws Exception Si se produce error al cargar los parametros para la conexi�n con Websphere MQ
	 */
	public ConectorClienteJMS(){
		try{
			this.initParameters();
			this.context = new JMSQueueManagerContextNojndi();
			//this.context.initConnection(configuracionMQ.getQueueManager(), configuracionMQ.getChannel(), configuracionMQ.getHost(), configuracionMQ.getPuertoServidor());
			//this.context.initConnection(configuracionMQ.getQueueManager(), ClassLoader.getSystemResource(configuracionMQ.getCcdtPath()));
			System.out.println(new URL(configuracionMQ.getCcdtPath()));
			this.context.initConnection(configuracionMQ.getQueueManager(), new URL(configuracionMQ.getCcdtPath()));
		}
		catch (Throwable e) {
			log.fatal("Error al iniciar el contexto de conexi�n", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Inicia toda la configuraci�n necesaria para la comunicaci�n con Websphere MQ mediante el Connection Factory proporcionado.
	 * @see #closeQManager()
	 * @see #closeQManagerQuietly()
	 * @since 1.0
	 * @throws Exception Si se produce error al cargar los parametros para la conexi�n con Websphere MQ
	 */
	public ConectorClienteJMS(QueueConnectionFactory mqConnFactory){
		try{
			this.initParameters();
			this.context = new JMSQueueManagerContextjndi();
			this.context.setConnFactory(mqConnFactory);
		}
		catch (Throwable e) {
			log.fatal("Error al iniciar el contexto de conexi�n", e);
			throw new RuntimeException(e);
		}
	}

	protected void initParameters() throws IOException {
		super.initParameters();
		this.jmsUserProperties = new HashMap();
	}
	
	
	public void closeQManager(){
		this.context.closeConnection();
	}
	
	public void closeQManagerQuietly(){
		try{
			this.context.closeConnection();
		}
		catch (Throwable e) {
			log.error("Error al cerrar la conexiones",e);
		}
	}
	
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
			AlarmaJMS.enviarAColaAlarmaMensajeJMS(this.context.getConnFactory(), configuracionMQ.getAlarma(), SimpleXMLHelper.getValueTag(ConstantesConector.HEADER_TAG_NAME, datosNegocio), Integer.toString(ex.reasonCode), TemplateMessageHelper.getMQError(ex.reasonCode, ex.completionCode, ex), datosNegocio, this.jmsUserProperties);
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
			AlarmaJMS.enviarAColaAlarmaMensajeJMS(this.context.getConnFactory(), configuracionMQ.getAlarma(), SimpleXMLHelper.getValueTag(ConstantesConector.HEADER_TAG_NAME, enviarMensaje.getXMLDatosGCSSIEnvelope()), Integer.toString(reason), ThrowableHelper.stackTrace2String(e), datosNegocio, this.jmsUserProperties);
		}
		log.error("Error al procesar la transaccion",e);
		
	}

	protected int enviarMensajeAsincrono(String datosNegocio) {
		int resultado = 0;
		try{
			enviarMensaje = new MensajeAsincronoJMS(this.context, this.configuracionMQ, this.parametrosCliente, datosNegocio);
			enviarMensaje.setMessageProperties(jmsUserProperties);
			escribirRequerimientoALog(enviarMensaje.getXMLDatosGCSSIEnvelope(), false);
			resultado = enviarMensaje.enviarMensajeJMS(iAleatorio, this.parametrosCliente.getPrioridad());
			log.debug("Transaccion completada con exito a " + parametrosCliente.getCodigoServicio() + "." + configuracionMQ.getDefinicionRequerimiento());
		}
		catch (JMSException e) {
			return resolveJMSException(datosNegocio, e);
		}
		catch (Throwable e) {
			enviarMensajeAdministrarException(datosNegocio, e, -1);
			return -1;
		}
		return resultado;
	}

	protected int enviarMensajeSincrono(String datosNegocio) {
		int resultado = 0;
		try{
			enviarMensaje = new MensajeSincronoJMS(this.context, this.configuracionMQ, this.parametrosCliente, datosNegocio);
			enviarMensaje.setMessageProperties(this.jmsUserProperties);
			escribirRequerimientoALog(enviarMensaje.getXMLDatosGCSSIEnvelope(), true);
			resultado = enviarMensaje.enviarMensajeJMS(iAleatorio, this.parametrosCliente.getPrioridad());
			this.bufferRespuesta = enviarMensaje.getDatosRespuesta();
			log.debug("Transaccion completada con exito a " + parametrosCliente.getCodigoServicio() + "." + configuracionMQ.getDefinicionRequerimiento());
			escribirRespuestaALog(this.bufferRespuesta);
		}
		catch (JMSException e) {
			return resolveJMSException(datosNegocio, e);
		}
		catch (Throwable e) {
			enviarMensajeAdministrarException(datosNegocio, e.getCause(), -1);
			return -1;
		}
		return resultado;
	}

	private int resolveJMSException(String datosNegocio, JMSException e) {
		int error = -1;
		if(e.getCause() instanceof MQException){
			MQException mqEx = (MQException)e.getCause();
			error = mqEx.reasonCode;
			enviarMensajeAdministrarException(datosNegocio, mqEx, error);
			return error;
		}
		else if(e.getErrorCode()!=null && !e.getErrorCode().equals("")){
			try{
				error = new Integer(e.getErrorCode()).intValue();
				enviarMensajeAdministrarException(datosNegocio, e, error);
			}
			catch (Exception e1) {
				try{
					DetailedMessageFormatException ex = (DetailedMessageFormatException)e;
					log.error("Esto debe dar mas datos", ex);
					enviarMensajeAdministrarException(datosNegocio, ex, error);
				}
				catch (Exception exc) {
				}
				return error;
			}
		}
		enviarMensajeAdministrarException(datosNegocio, e, error);
		return error;
	}
	
	
	public static String sendSynchronousMessageToESB(String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, int tiempoExpiracion, String nombreAplicacion) throws Exception{
		ConectorClienteBase conector = null;
		try {
			conector = new ConectorClienteJMS();
			return sendSynchronousMessageToESB(conector, servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, tiempoExpiracion, nombreAplicacion, null, null);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			conector.closeQManagerQuietly();
		}
	}
	
	public static void sendAsynchronousMessageToESB(String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, String nombreAplicacion){
		ConectorClienteBase conector = null;
		try {
			conector = new ConectorClienteJMS();
			sendAsynchronousMessageToESB(conector, servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, nombreAplicacion, null, null);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			conector.closeQManagerQuietly();
		}
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
	 * @param jmsUserProperties Mapa con las propiedades de usuario que se colocar�n al mensaje JMS
	 */
	public static void sendAsynchronousMessageToESB(String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, String nombreAplicacion, Map jmsUserProperties){
		ConectorClienteBase conector = null;
		try {
			conector = new ConectorClienteJMS();
			sendAsynchronousMessageToESB(conector, servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, nombreAplicacion, jmsUserProperties, null);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			conector.closeQManagerQuietly();
		}
	}
	
	/**
	 * Envia un mensaje en modalidad asincrona a ESB, encapsula la funcionalidad extendia del conector cliente.
	 * @param nombreJndi Nombre del recurso JNDI con la fabrica de conexiones
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
	 * @param jmsUserProperties Mapa con las propiedades de usuario que se colocar�n al mensaje JMS
	 */
	public static void sendAsynchronousMessageToESB(String nombreJndi, String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, String nombreAplicacion, Map jmsUserProperties){
		ConectorClienteBase conector = null;
		try {
			conector = new ConectorClienteJMS(nombreJndi);
			sendAsynchronousMessageToESB(conector, servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, nombreAplicacion, jmsUserProperties, null);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
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
	 * @param jmsUserProperties Mapa con las propiedades de usuario que se colocar�n al mensaje JMS
	 * @param jmsSelector Selector que se utilizar� para hacer el get de la respuesta
	 * @return Datos obtenidos de la respuesta desde ESB
	 */
	public static String sendSynchronousMessageToESB(String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, int tiempoExpiracion, String nombreAplicacion, Map jmsUserProperties, String jmsSelector){
		ConectorClienteBase conector = null;
		try {
			conector = new ConectorClienteJMS();
			return sendSynchronousMessageToESB(conector, servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, tiempoExpiracion, nombreAplicacion, jmsUserProperties, jmsSelector);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			conector.closeQManagerQuietly();
		}
	}
	
	/**
	 * Envia un mensaje en modalidad sincrona a ESB, encapsula la funcionalidad extendia del conector cliente.
	 * @param nombreJndi Nombre del recurso JNDI con la fabrica de conexiones
	 * @param servicio C�digo de serivico a ser utilizado
	 * @param pais Pa�s desde donde se genera la transaccion
	 * @param compania Compa�ia para la que se genera la transaccion
	 * @param cadena Cadena para la que se genera la transaccion
	 * @param sucursal Sucursal para la que se genera la transaccion
	 * @param numeroTransaccion Numero de transaccion (campo solo descriptivo)
	 * @param hostOrigen IP/Nombre del servidor desde el que se genera la transaccion
	 * @param token Token (campo solo descriptivo)
	 * @param datos Datos de negocio
	 * @param tiempoExpiracion Tiempo de espera por la respuesta a la peticion
	 * @param nombreAplicacion Nombre del modulo/aplicacion que genera la transaccion
	 * @param jmsUserProperties Mapa con las propiedades de usuario que se colocar�n al mensaje JMS
	 * @param jmsSelector Selector que se utilizar� para hacer el get de la respuesta
	 * @return Datos obtenidos de la respuesta desde ESB
	 */
	public static String sendSynchronousMessageToESB(String nombreJndi, String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, int tiempoExpiracion, String nombreAplicacion, Map jmsUserProperties, String jmsSelector){
		ConectorClienteBase conector = null;
		try {
			conector = new ConectorClienteJMS(nombreJndi);
			return sendSynchronousMessageToESB(conector, servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, tiempoExpiracion, nombreAplicacion, jmsUserProperties, jmsSelector);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			conector.closeQManagerQuietly();
		}
	}
	
	private static void sendAsynchronousMessageToESB(ConectorClienteBase conector, String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, String nombreAplicacion, Map jmsUserProperties, String jmsSelector) throws Exception{
		int reason = 0;
		setPropertiesAndSelector(conector, jmsUserProperties, jmsSelector);
		reason = conector.envioMensaje(servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, ConstantesConector.NO_WAIT_TIME, nombreAplicacion);
		if (reason != 0){
			throw new Exception("Error durante el envio del mensaje: " + reason);
		}
	}
	
	private static String sendSynchronousMessageToESB(ConectorClienteBase conector, String servicio, String pais, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datos, int tiempoExpiracion, String nombreAplicacion, Map jmsUserProperties, String jmsSelector) throws Exception{
		int reason = 0;
		setPropertiesAndSelector(conector, jmsUserProperties, jmsSelector);
		reason = conector.envioMensaje(servicio, pais, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datos, tiempoExpiracion, nombreAplicacion);
		if (reason != 0){
			throw new Exception("Error durante el envio del mensaje: " + reason);
		}
		return conector.getBufferRespuesta();
	}
	
	private static void setPropertiesAndSelector(ConectorClienteBase conector, Map jmsUserProperties, String jmsSelector) {
		if(jmsUserProperties!=null && !jmsUserProperties.isEmpty()) conector.setJmsUserProperties(jmsUserProperties);
		if(jmsSelector!=null && !jmsSelector.equals("")) conector.setSelectorJms(jmsSelector);
	}

	/**
	 * Retorna el objeto que contiene todo el contexto de conexi�n hacia Websphere MQ a trav�s de JMS
	 * @return instacia actual de {@link JMSQueueManagerContext} 
	 */
	public JMSQueueManagerContext getContext() {
		return context;
	}
	
}