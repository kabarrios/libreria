package com.ceiba.biblioteca.conectorCliente.MQ.principal;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.utilitarios.utilitarios.ConstantesConector;
import com.ceiba.biblioteca.utilitarios.utilitarios.PropertiesHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.StringHelper;
import com.ceiba.biblioteca.utilitarios.utilitarios.log.ConectorLogger;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.mq.constants.CMQC;


/**
 * <p>Clase que proporciona los elementos y definiciones b�sicas para que un usuario interactue con Websphere MQ.</p>
 * @author echicas
 */
public abstract class ConectorClienteBase implements Serializable{
	
	private static final long serialVersionUID = 1436031147831783488L;

	private static Log log = LogFactory.getLog(ConectorClienteBase.class);
	
	/**
	 * Constante con valor cero, utilizada para especificar el envio de un mensaje as�ncrono
	 * @see #envioMensaje(String, String, String, String, String, String, String, String, String, int, String)
	 * @see #envioMensaje(String, String, String, String, String, String, String, String, String, int, String, String, String) 
	 */
	public static final int NO_WAIT_TIME = 0;
	
	/** permite establecer si se envian los datos al m�todo remueve el grupo de caracteres no validos de un xml */ 
	protected boolean removerCaracteresXmlNoValidos = true;
	
	/** almacena la configuracion para el conector cliente */
	protected ParametrosConfiguracionMQ configuracionMQ;
	
	/** almacena los datos que seran colocados en el encabezado del la envoltura xml de integracion */
	protected ParametrosMensajeCliente parametrosCliente;
	
	/** variable utilizada para rescatar mensaje de respuesta */
	protected String bufferRespuesta;
	
	/** variable aleatoria utilizada para los grupos de mensajes */
	protected int iAleatorio = 0;
	
	/** variable utilizada para almacenar las propiedades de usuario que se colocaran en los mensajes enviados con jms */
	protected Map jmsUserProperties;
	
	/**
	 * M�todo que envia un mensaje a Websphere MQ.
	 * <p>Permite el envio tanto de mensajes s�ncronos como as�ncronos en base al parametro tiempoExpiracion, 
	 * si este es <code>0</code> no se espera respuesta, si es mayor que <code>0</code> se espera respuesta, si es menor que <code>0</code> se genera una excepci�n {@link InvalidParameterException}</p>
	 * @param codigoServicio C�digo de serivico a ser utilizado
	 * @param paisOrigen Pa�s desde donde se genera la transacci�n
	 * @param compania Compa�ia para la que se genera la transacci�n
	 * @param cadena Cadena para la que se genera la transacci�n
	 * @param sucursal Sucursal para la que se genera la transacci�n
	 * @param numeroTransaccion N�mero de transacci�n (campo solo descriptivo)
	 * @param hostOrigen IP/Nombre del servidor desde el que se genera la transacci�n
	 * @param token Token (campo solo descriptivo)
	 * @param datosNegocio Datos de negocio
	 * @param tiempoExpiracion Tiempo de espera por la respuesta a la petici�n en segundos
	 * @param nombreAplicacion Nombre del modulo/aplicaci�n que genera la transacci�n
	 * @return C�digo que indica exito (<code>0</code>) o error con el c�digo de error Websphere MQ 
	 */
	public int envioMensaje(String codigoServicio, String paisOrigen, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datosNegocio, int tiempoExpiracion, String nombreAplicacion){
		int resultado = 0;
		try{
			//cargamos a un objeto este monton de parametros (da weba andar pasandolos por parametros a todos)
			cargarParametrosCliente(codigoServicio, paisOrigen, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datosNegocio, tiempoExpiracion, nombreAplicacion);
			//validamos los parametros
			assertParametrosCliente();
			//verificamos el tipo de mensaje
			if (parametrosCliente.getTiempoExpiracion() == ConstantesConector.NO_WAIT_TIME) {
				resultado = enviarMensajeAsincrono(replaceInvalidCharacter(datosNegocio));
			} else {
				parametrosCliente.setTiempoExpiracion(tiempoExpiracion);
				resultado = enviarMensajeSincrono(replaceInvalidCharacter(datosNegocio));
			}
		}
		catch (Throwable e) {
			log.fatal("Error al enviar el mensaje", e);
			throw new RuntimeException(e);
		}
		return resultado;
	}
	
	/**
	 * <p>M�todo que envia un mensaje a Websphere MQ que pertenece a un grupo de mensajes.</p>
	 * <p>Permite el envio tanto de mensajes s�ncronos como as�ncronos en base al parametro tiempoExpiracion, 
	 * si este es <code>0</code> no se espera respuesta, si es mayor que <code>0</code> se espera respuesta, si es menor que <code>0</code> se genera una excepci�n {@link InvalidParameterException}</p>
	 * @param codigoServicio C�digo de serivico a ser utilizado
	 * @param paisOrigen Pa�s desde donde se genera la transacci�n
	 * @param compania Compa�ia para la que se genera la transacci�n
	 * @param cadena Cadena para la que se genera la transacci�n
	 * @param sucursal Sucursal para la que se genera la transacci�n
	 * @param numeroTransaccion N�mero de transacci�n (campo solo descriptivo)
	 * @param hostOrigen IP/Nombre del servidor desde el que se genera la transacci�n
	 * @param token Token (campo solo descriptivo)
	 * @param datosNegocio Datos de negocio
	 * @param tiempoExpiracion Tiempo de espera por la respuesta a la petici�n en segundos
	 * @param nombreAplicacion Nombre del modulo/aplicaci�n que genera la transacci�n
	 * @param grupoMensaje Cadena que identifica al grupo de mensajes
	 * @param totalMensajes Cantidad total de mensajes que se enviar� como grupo de mensajes
	 * @return C�digo que indica exito (<code>0</code>) o error con el c�digo de error Websphere MQ 
	 */
	public int envioMensaje(String codigoServicio, String paisOrigen, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datosNegocio, int tiempoExpiracion, String nombreAplicacion, String grupoMensaje, String totalMensajes){
		parametrosCliente.setGrupoMensaje(grupoMensaje);
		parametrosCliente.setTotalMensajes(totalMensajes);
		return envioMensaje(codigoServicio, paisOrigen, compania, cadena, sucursal, numeroTransaccion, hostOrigen, token, datosNegocio,	tiempoExpiracion, nombreAplicacion);
	}
	
	/**
	 * M�todo que envia mensajes as�ncronos a Websphere MQ dado el contexto iniciado en el constructor
	 * @param datosNegocio Datos que pertenecen al usuario (cliente) y que son los que corresponden a la transacci�n de negocio
	 * @return <code>0</code> si la transacci�n se completo con exito, <code>-1<code> en caso de un error no generado por Websphere MQ 
	 * y cualquier codigo de error correspondiente a Webpshere MQ. 
	 */
	protected abstract int enviarMensajeAsincrono(String datosNegocio);
	
	/**
	 * M�todo que envia mensajes s�ncronos a Websphere MQ dado el contexto iniciado en el constructor {@l
	 * @param datosNegocio Datos que pertenecen al usuario (cliente) y que son los que corresponden a la transacci�n de negocio
	 * @return <code>0</code> si la transacci�n se completo con exito, <code>-1<code> en caso de un error no generado por Websphere MQ 
	 * y cualquier codigo de error correspondiente a Webpshere MQ. 
	 */
	protected abstract int enviarMensajeSincrono(String datosNegocio);
	
	/**
	 * M�todo que cierra la conexi�n al Queue Manager 
	 */
	public abstract void closeQManager();
	
	/**
	 * M�todo que cierra la conexi�n al Queue Manager sin propagar excepciones 
	 */
	public abstract void closeQManagerQuietly();
	
	
	/**
	 * <p>M�todo que inicia todos los objetos para operar.</p> 
	 * <p>Lee el properties con los datos para la conexion MQ e instancia el objeto que contiene los parametros proporcionados por el usuario</p>
	 * @throws IOException
	 */
	protected void initParameters() throws IOException {
		// inicializamos el numero aleatio con una semilla, antes no se inicializaba asi
		iAleatorio = new Random(new GregorianCalendar().getTime().getTime()).nextInt();
		
		//se inicializa la variable que contrendra los datos del Header en el xml
		parametrosCliente = new ParametrosMensajeCliente();
		parametrosCliente.setGrupoMensaje(new String());
		parametrosCliente.setTotalMensajes(new String());
		
		//leemos la configuracion para la conexion a mq y generacion de logs
		configuracionMQ = (ParametrosConfiguracionMQ) PropertiesHelper.properties2Bean(PropertiesHelper.getProperties(ConstantesConector.ARCHIVO_CONFIGURACION_CLIENTE), ParametrosConfiguracionMQ.class);
	}
	
	/**
	 * Determina si se escribe a log el requerimiento enviado a Websphere MQ y que plantilla utilizar pues puede variar si el mensaje es s�ncrono o as�ncrono
	 * @param datos Datos a escribir a log (GCS_SI envelope)
	 * @param isSincrono Determina la plantilla a utilizar
	 */
	protected void escribirRequerimientoALog(String datos, boolean isSincrono){
		if(this.parametrosCliente.isEscribirLog()){
			if(isSincrono){
				ConectorLogger.escribirRequerimientoRRClienteALog(this.configuracionMQ.getRutaLog(), this.parametrosCliente.getNombreAplicacionOrigen(), this.configuracionMQ.getTransaccionLogger(), this.parametrosCliente.getCodigoServicio(), this.parametrosCliente.getPaisOrigen(), datos);
			}
			else{
				ConectorLogger.escribirRequerimientoSFClienteALog(this.configuracionMQ.getRutaLog(), this.parametrosCliente.getNombreAplicacionOrigen(), this.configuracionMQ.getTransaccionLogger(), this.parametrosCliente.getCodigoServicio(), this.parametrosCliente.getPaisOrigen(), datos);
			}
		}
	}
	
	/**
	 * Determina si se escribe a log la respuesta obtenida de la transaccion s�ncrona
	 * @param datos Datos a escribir en el log (GCS_SI envelope)
	 */
	protected void escribirRespuestaALog(String datos){
		if(this.parametrosCliente.isEscribirLog()){
			ConectorLogger.escribirRespuestaRRClienteALog(this.configuracionMQ.getRutaLog(), this.parametrosCliente.getNombreAplicacionOrigen(), this.configuracionMQ.getTransaccionLogger(), this.parametrosCliente.getCodigoServicio(), this.parametrosCliente.getPaisOrigen(), datos);
		}
	}
	
	/**
	 * <p>Carga los parametros proporcionados por el cliente a un objeto para facilitar el uso y portabilidad de estos.</p>
	 * <p>Ademas establece si el mensaje es persitente y si se escribir� a log.</p> 
	 * @param codigoServicio C�digo de serivico a ser utilizado
	 * @param paisOrigen Pa�s desde donde se genera la transacci�n
	 * @param compania Compa�ia para la que se genera la transacci�n
	 * @param cadena Cadena para la que se genera la transacci�n
	 * @param sucursal Sucursal para la que se genera la transacci�n
	 * @param numeroTransaccion N�mero de transacci�n (campo solo descriptivo)
	 * @param hostOrigen IP/Nombre del servidor desde el que se genera la transacci�n
	 * @param token Token (campo solo descriptivo)
	 * @param datosNegocio Datos de negocio
	 * @param tiempoExpiracion Tiempo de espera por la respuesta a la petici�n en segundos
	 * @param nombreAplicacion Nombre del modulo/aplicaci�n que genera la transacci�n
	 */
	protected void cargarParametrosCliente(String codigoServicio, String paisOrigen, String compania, String cadena, String sucursal, String numeroTransaccion, String hostOrigen, String token, String datosNegocio, int tiempoExpiracion, String nombreAplicacion){
		parametrosCliente.setMensajePersistente(evaluarPersistencia(codigoServicio));
		parametrosCliente.setEscribirLog(evaluarLog(codigoServicio));
		parametrosCliente.setCodigoServicio(codigoServicio);
		parametrosCliente.setPaisOrigen(paisOrigen);
		parametrosCliente.setCompania(compania);
		parametrosCliente.setCadena(cadena);
		parametrosCliente.setSucursal(sucursal);
		parametrosCliente.setHostOrigen(hostOrigen);
		parametrosCliente.setToker(token);
		parametrosCliente.setNumeroTransaccion(numeroTransaccion);
		parametrosCliente.setTiempoExpiracion(tiempoExpiracion);
		parametrosCliente.setNombreAplicacionOrigen(nombreAplicacion!=null?nombreAplicacion.toUpperCase():new String()); // se utiliza la variable local no la de MQContexto
		parametrosCliente.setPrioridad(CMQC.MQPRI_PRIORITY_AS_Q_DEF);
	}
	
	/**
	 * <p>Valida los parametros proporcionados por el cliente.</p>
	 * <p>Los parametros requeridos son:
	 * <ul>
	 * <li>{@link ParametrosMensajeCliente#setCodigoServicio(String)}</li>
	 * <li>{@link ParametrosMensajeCliente#()}</li>
	 * <li>{@link ParametrosMensajeCliente#setNombreAplicacionOrigen(String)}</li>
	 * </ul>
	 * <p> 
	 */
	protected void assertParametrosCliente(){
		StringBuffer error = new StringBuffer();
		if(parametrosCliente.getCodigoServicio() == null || parametrosCliente.getCodigoServicio().equals("")){
			 error.append("El c�digo de servicio no puede ser null o vacio").append("\n");
		}
		if(parametrosCliente.getTiempoExpiracion() < 0){
			error.append("Error, se debe especificar un tiempo igual o superior a cero para poder enviar un mensaje a Websphere MQ").append("\n");
		}
		if(parametrosCliente.getNombreAplicacionOrigen()==null ||  parametrosCliente.getNombreAplicacionOrigen().equals("")){
			error.append("El nombre de la aplicacion origen no puede ser null o vacio").append("\n");
		}
		if(error.length()>0){
			throw new InvalidParameterException(error.toString());
		}
	}
	
	/**
	 * Evalua si el c�digo de servicio proporcionado es tratado con persistencia dentro de Webpshere MQ
	 * @param codigoServicio c�digo de servicio a evaluar
	 * @return <code>true</code> si es persistente, <code>false</code> en caso contrario
	 */
	protected boolean evaluarPersistencia(String codigoServicio) {
		try{
			Properties prop = PropertiesHelper.getProperties(ConstantesConector.ARCHIVO_PERSISTENCIA);
			if(prop!=null && prop.containsKey(codigoServicio)){
				log.info("Mensaje persistente para el servicio " + codigoServicio);
				return true;
			}
		}
		catch (Exception e) {
			log.error("Error al evaluar la persistencia del mensaje, se procesara como no persistente",e);
		}
		log.info("Mensaje no persistente para el servicio " + codigoServicio);
		return false;
	}
	
	/**
	 * Evalua si el c�digo de servicio proporcionado esta configurado para ser enviado a log
	 * @param codigoServicio c�digo de servicio a evaluar
	 * @return <code>true</code> si se debe enviar a log, <code>false</code> en caso contrario
	 */
	protected boolean evaluarLog(String codigoServicio) {
		 try{
			 Properties prop = PropertiesHelper.getProperties(ConstantesConector.ARCHIVO_LOG);
			 if(prop!=null && prop.containsKey(codigoServicio)){
				log.info("Mensaje a archivo de log para el servicio " + codigoServicio);
				return true;
			}
		}
		catch (Exception e) {
			log.error("Error al evaluar la escritura a log del mensaje, ciertamente es un error pero no detiene el proceso",e);
		}
		log.info("Mensaje no se escribe a log para el servicio " + codigoServicio);
		return false;
	}
	
	/**
	 * <p>Reemplaza los caracteres no validos en un xml y que esten configurados en el archivo </p>
	 * <p>Evalua si la propiedade } existe en el properties y si {@link #removerCaracteresXmlNoValidos} es <code>true</code></p>
	 * <p>Ademas evalua si el c�digo de servicio existe dentro de dicho properties para remover caracteres especificos para el servicio solicitado</p>  
	 * @param datosNegocio #String (xml) sobre el que se quitaran los caracteres
	 * @return #String sin caracteres no validos en xml y sin caracteres especificados para el servicio solicitado
	 */
	protected String replaceInvalidCharacter(String datosNegocio) {
		if (datosNegocio == null) return "";
		String temp = new String();
		try {
			Properties prop = PropertiesHelper.getProperties(ConstantesConector.REGEX_PROPERTIES);
			if(prop.containsKey(ConstantesConector.INVALID_CHAR_REGEX) && removerCaracteresXmlNoValidos){
				temp = StringHelper.removeCharacter(datosNegocio, prop.getProperty(ConstantesConector.INVALID_CHAR_REGEX));
				log.info("Se aplico filtro para caracteres no validos en xml sobre los datos del cliente");
			}
			else{
				log.debug("No se aplico filtro general de caracteres sobre los datos de negocio [ invalid.char=" + prop.getProperty(ConstantesConector.INVALID_CHAR_REGEX) +  "]");
			}
			if (prop.containsKey(parametrosCliente.getCodigoServicio())) {
				log.info("Reemplazando caracteres no validos [" + prop.getProperty(parametrosCliente.getCodigoServicio()) + "] para el servicio " + parametrosCliente.getCodigoServicio() + ". Estos caracteres provocan comportamientos extra�os en el servicio dentro de ESB y/o en el servicio de backend, favor verifique el proceso que genera sus datos y corrijalo");
				temp = StringHelper.removeCharacter(temp, prop.getProperty(parametrosCliente.getCodigoServicio()));
			}
		} catch (Exception e) {
			log.error("Error al intentar verificar si se eliminaran caracteres no validos, no se modificaran los datos de negocio",e);
			return datosNegocio;
		}
		return temp;
	}
	
	/**
	 * Envia a alarma el error y evalua el tipo de Excepci�n/Error para determinar el tipo de plantilla para log se utilizar�.
	 * En los logs de error/envio a Alarma no se discrima servicio, se envian todos sin excepci�n
	 * @param datosNegocio Datos a escribir en el log
	 * @param e para determinar el tipo de excepci�n y plantilla de log a utilizar
	 * @param reason c�digo que identifica el tipo de error (Websphere MQ u otro) para ser utilizado en el envio a alarma
	 * @param
	 */
	protected abstract void enviarMensajeAdministrarException(String datosNegocio, Throwable e, int reason);
	
	/**
	 * Retorna el valor actual de la variable que determina si se remueven los caracteres no validos en un xml
	 * @return the removerCaracteresXmlNoValidos
	 */
	protected boolean isRemoverCaracteresXmlNoValidos() {
		return removerCaracteresXmlNoValidos;
	}

	/**
	 * Establece el valor de la variable que determina si se remueven los caracteres no validos en un xml
	 * @param removerCaracteresXmlNoValidos the removerCaracteresXmlNoValidos to set
	 */
	protected void setRemoverCaracteresXmlNoValidos(boolean removerCaracteresXmlNoValidos) {
		this.removerCaracteresXmlNoValidos = removerCaracteresXmlNoValidos;
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
	 * Estable la prioridad que se le colocar� al mensaje mq que se enviar�
	 * @param prioridad Prioridad que se le dar� al mensaje mq
	 */
	public void setPrioridad(int prioridad){
		this.parametrosCliente.setPrioridad(prioridad);
	}

	/**
	 * A�ade una propiedad al mensaje jms que ser� enviado a MQ
	 * @param key Llave de la propiedad
	 * @param value {@link Object} con el valor de la propiedad 
	 */
	public void putJmsUserProperty(String key, Object value){
		if(this.jmsUserProperties==null){
			this.jmsUserProperties = new HashMap();
		}
		this.jmsUserProperties.put(key, value);
	}

	/**
	 * Retorna las propiedades establecidas para un mensaje JMS
	 * @return {@link Map} con las propiedades establecidas por el cliente
	 */
	public Map getJmsUserProperties() {
		return jmsUserProperties;
	}

	/**
	 * Establece las propiedades del usuario en un mensaje JMS 
	 * @param jmsUserProperties propiedades JMS de usuario
	 */
	public void setJmsUserProperties(Map jmsUserProperties) {
		this.jmsUserProperties = jmsUserProperties;
	}
	
	/**
	 * Establece el selector del usuario a utilizar cuando los mensajes JMS son sicronos
	 * @param selector query JMS para el get de los mensajes
	 */
	public void setSelectorJms(String selector){
		this.parametrosCliente.setSelectorJms(selector);
	}
	
	public String getSelectorJms(){
		return parametrosCliente.getSelectorJms();
	}
	
}
