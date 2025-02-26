package com.ceiba.biblioteca.conectorCliente.MQ.cliente;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.infraestructure.service.PrestamoService;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.jms.JMSQueueManagerContext;
import com.ibm.jms.JMSTextMessage;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.jms.JmsConstants;
import com.ibm.msg.client.wmq.WMQConstants;


import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.TextMessage;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase que implementa y define la forma de interactaur con Webshpere MQ a trav�s de JMS
 * @version 1.0
 * @author echicas
 */
public abstract class EnviarMensajeJMS extends EnviarMensaje implements Serializable {
	
	private static final long serialVersionUID = -4069355785449059985L;
	
	/**
	 * Contexto JMS que contiene el {@link ConnectionFactory}
	 */
	protected JMSQueueManagerContext jmsContext;
	/**
	 * Variable que contiene las propiedades de usuario que se enviar�n en el mensaje
	 */
	protected Map messageProperties;
	
	/**
	 * Inicia todas las configuraciones e inicia el objeto {@link QueueConnection}
	 * @param jmsContext contexto JMS a ser utilizado en la instancia
	 * @param parametros parametros de configuraci�n del conector cliente
	 * @param headerCliente parametros de configuraci�n proporcionados por el usuario
	 * @param datosCliente datos a ser enviados por el usuario en el mensaje JMS
	 * @throws JMSException Se origina en cualquier error producido por la implementaci�n JMS en uso
	 */
	public EnviarMensajeJMS(JMSQueueManagerContext jmsContext, ParametrosConfiguracionMQ parametros, ParametrosMensajeCliente headerCliente, String datosCliente) throws JMSException {
		super();
		this.configuracionMQ = parametros;
		this.parametrosCliente = headerCliente;
		this.jmsContext = jmsContext;
		this.datosCliente = datosCliente;
		assertConstructor();
		this.datosGCSSIEnvelopeReq = getXMLDatosGCSSIEnvelope();
	//	this.jmsContext.getConnection().start();
	}
	
	/**
	 * No visible para obligar que se instancia con todos los parametros necesarios para operar
	 */
	private EnviarMensajeJMS(){
	}
	
	/**
	 * A�ade todos las propiedades de usuario al mensaje JMS que se enviar� a MQ
	 * @param message Mensaje que se enviar� a MQ
	 * @throws JMSException Se origina al momento de dar error cualquier {@link TextMessage#setObjectProperty(String, Object)}
	 */
	public void addProperties2JMSMessage(TextMessage message) throws JMSException{
		if(this.messageProperties!=null && !this.messageProperties.isEmpty()){
			for(Iterator entries = this.messageProperties.entrySet().iterator(); entries.hasNext();){
				Entry entry = (Entry) entries.next();
				message.setObjectProperty((String)entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * Valida que no se envien parametros null al constructor pues todos son necesarios para el correcto funcionamineto
	 */
	protected void assertConstructor(){
		if(this.configuracionMQ==null || this.parametrosCliente==null || this.jmsContext==null || this.datosCliente==null) {
			throw new InvalidParameterException("Ningun parametro puede ser null, favor verifique sus datos");
		}
	}

	/**
	 * Coordina el envio de mensajes a Websphere MQ y si el mensaje es sincrono la recepci�n de la respectiva respuesta
	 * @param aleatorio N�mero identificador de la transacci�n, usualmente utilizado para grupos de mensajes
	 * @return <code>0<code> si la transacci�n es exitosa � un c�digo de error si es una excepci�n no tratable desde MQ, en caso contrario retornara el <code>reasonCode</code> correspondiente a la excepci�n MQ.
	 */
	public abstract int enviarMensajeJMS(int aleatorio, int priority) throws JMSException;
	
	/**
	 * Coloca un mensaje en modo cliente en una cola de Websphere MQ.
	 * @param queueReqName Nombre de la cola donde ser� colocado el requerimiento ya sea s�ncrono o as�ncrono
	 * @param replyToQueueName Nombre de la cola donde se esperar� la respuesta en caso de ser mensaje s�ncrono
	 * @param aleatorio N�mero identificador de la transacci�n, usualmente utilizado para grupos de mensajes
	 */
	protected abstract void messagePutJMS(String queueReqName, String replyToQueueName, int aleatorio, int priority) throws Exception;
	
	/**
	 * Obtiene un mensaje en modo cliente de una cola de Websphere MQ, normalmente la respuesta a una petici�n s�ncrona.
	 * @param queueRespName Nombre de la cola donde se extraer� el mensaje.
	 */
	protected abstract void messageGetJMS(String queueRespName) throws JMSException;

	/**
	 * Establece propiedades por defecto para un {@link MQQueue}.
	 * <p>Propiedades con valores por defecto:<p>
	 * <ul>
	 * <li>{@link MQQueue#setExpiry(long)}: ilimitado</li>
	 * <li>{@link MQQueue#setFailIfQuiesce(int)}: s�</li>
	 * <li> {@link MQQueue#setEncoding(int)}: nativo</li>
	 * </ul>
	 * @param queue instancia de {@link MQQueue}
	 * @throws JMSException Se produce por cualquier set utilizado en este m�todo
	 */
	protected void setQueueDefaults(MQQueue queue) throws JMSException{
		queue.setExpiry(WMQConstants.WMQ_EXP_UNLIMITED);
		queue.setFailIfQuiesce(WMQConstants.WMQ_FIQ_YES);
		queue.setEncoding(WMQConstants.WMQ_ENCODING_NATIVE);
	}

	/**
	 * Retorna la propiedades establecidas por el usuario para el mensaje JMS
	 * @return {@link Map} con las propiedades del usuario
	 */
	public Map getMessageProperties() {
		return messageProperties;
	}

	/**
	 * Establece las propiedades del usuario para el mensaje JMS
	 * @param messageProperties {@link Map} con las propiedades que el usuario adjuntar� al mensaje
	 */
	public void setMessageProperties(Map messageProperties) {
		this.messageProperties = messageProperties;
	}
	
	public void messageGroupSettings(int aleatorio, Object message) {
		try{
			JMSTextMessage msg = (JMSTextMessage)message;
			if(this.parametrosCliente.getGrupoMensaje()!=null && !this.parametrosCliente.getGrupoMensaje().equals("")){
				msg.setStringProperty(JmsConstants.JMSX_GROUPID,  new String(this.parametrosCliente.getNumeroTransaccion() + aleatorio));
				msg.setIntProperty(JmsConstants.JMSX_GROUPSEQ, new Integer(this.parametrosCliente.getGrupoMensaje()).intValue());
				
				if(!this.parametrosCliente.getGrupoMensaje().equals(this.parametrosCliente.getTotalMensajes())){
					msg.setBooleanProperty(JmsConstants.JMS_IBM_LAST_MSG_IN_GROUP, false);
				}
				else{
					msg.setBooleanProperty(JmsConstants.JMS_IBM_LAST_MSG_IN_GROUP, true);
				}
			}
		}
		catch (JMSException e) {
			throw new RuntimeException(e.getLinkedException()!=null?e.getLinkedException():e);
		}
	}

}
