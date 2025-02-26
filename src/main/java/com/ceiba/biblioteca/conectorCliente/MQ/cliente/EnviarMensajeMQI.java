package com.ceiba.biblioteca.conectorCliente.MQ.cliente;

import com.ceiba.biblioteca.conectorCliente.MQ.contexto.ParametrosMensajeCliente;
import com.ceiba.biblioteca.utilitarios.utilitarios.mq.ParametrosConfiguracionMQ;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * Clase que implementa y define la forma de interactaur con Webshpere MQ a trav�s de MQI
 * @version 1.0
 * @author echicas
 */
public abstract class EnviarMensajeMQI extends EnviarMensaje implements Serializable {
	
	private static final long serialVersionUID = 2991492060244578315L;
	/** Variable para almacenar la conexi�n a Websphere MQ */
	protected MQQueueManager mqQueueManager;
	/** Opciones de apertura de una cola Websphere MQ para colocar un mensaje */
	protected int openOptionsReq = CMQC.MQOO_OUTPUT + CMQC.MQOO_FAIL_IF_QUIESCING + CMQC.MQOO_SET_IDENTITY_CONTEXT;
	/** Opciones de apertura de una cola Websphere MQ para extraer un mensaje */
	protected int openOptionsResp = CMQC.MQGMO_CONVERT | CMQC.MQOO_FAIL_IF_QUIESCING | CMQC.MQGMO_WAIT;
	
	/**
	 * Inicializa las configuraciones necesarias para el envio/recepci�n de mensajes mediante MQI a Websphere MQ.
	 * @param mqQueueManager Instancia con la conexi�n a Websphere MQ abierta.
	 * @param parametros Configuraci�n del Conector Cliente
	 * @param headerCliente Parametros proporcionados por el usuario para interactuar con Websphere MQ
	 * @param datosCliente �rea de datos del cliente
	 */
	public EnviarMensajeMQI(MQQueueManager mqQueueManager, ParametrosConfiguracionMQ parametros, ParametrosMensajeCliente headerCliente, String datosCliente){
		super();
		this.configuracionMQ = parametros;
		this.parametrosCliente = headerCliente;
		this.mqQueueManager = mqQueueManager;
		this.datosCliente = datosCliente;
		assertConstructor();
		this.datosGCSSIEnvelopeReq = getXMLDatosGCSSIEnvelope();
	}
	
	/**
	 * Valida que no se envien parametros null al constructor pues todos son necesarios para el correcto funcionamineto
	 */
	protected void assertConstructor(){
		if(this.configuracionMQ==null || this.parametrosCliente==null || this.mqQueueManager==null || this.datosCliente==null) {
			throw new InvalidParameterException("Ningun parametro puede ser null, favor verifique sus datos");
		}
	}
	
	/**
	 * No visible para obligar que se instanci con todos los parametros necesarios para operar
	 */
	private EnviarMensajeMQI(){
	}
	
	/**
	 * Coordina el envio de mensajes a Websphere MQ y si el mensaje es sincrono la recepci�n de la respectiva respuesta
	 * @param aleatorio N�mero identificador de la transacci�n, usualmente utilizado para grupos de mensajes
	 * @return <code>0<code> si la transacci�n es exitosa � un c�digo de error si es una excepci�n no tratable desde MQ, en caso contrario retornara el <code>reasonCode</code> correspondiente a la excepci�n MQ.
	 * @throws Exception Propagada por cualquier acci�n excepto por las relacionadas con Websphere MQ
	 * @throws MQException Propagada por la interacci�n con Websphere MQ
	 */
	public abstract int enviarMensaje(int aleatorio, int priority) throws Exception, MQException;
	
	/**
	 * Coloca un mensaje en modo cliente en una cola de Websphere MQ.
	 * @param queueReqName Nombre de la cola donde ser� colocado el requerimiento ya sea s�ncrono o as�ncrono
	 * @param replyToQueueName Nombre de la cola donde se esperar� la respuesta en caso de ser mensaje s�ncrono
	 * @param aleatorio N�mero identificador de la transacci�n, usualmente utilizado para grupos de mensajes
	 * @throws Exception Propagada por cualquier acci�n excepto por las relacionadas con Websphere MQ
	 * @throws MQException Propagada por la interacci�n con Websphere MQ
	 */
	protected abstract void messagePut(String queueReqName, String replyToQueueName, int aleatorio, int priority) throws Exception, MQException;
	
	/**
	 * Obtiene un mensaje en modo cliente de una cola de Websphere MQ, normalmente la respuesta a una petici�n s�ncrona.
	 * @param queueRespName Nombre de la cola donde se extraer� el mensaje.
	 * @throws Exception Propagada por cualquier acci�n excepto por las relacionadas con Websphere MQ
	 * @throws MQException Propagada por la interacci�n con Websphere MQ
	 */
	protected abstract void messageGet(String queueRespName) throws Exception, MQException;
	
	public void messageGroupSettings(int aleatorio, Object message){
		MQMessage mqMessage = (MQMessage)message;
		mqMessage.messageFlags = !this.parametrosCliente.getGrupoMensaje().equals(this.parametrosCliente.getTotalMensajes()) ? CMQC.MQMF_MSG_IN_GROUP : CMQC.MQMF_LAST_MSG_IN_GROUP;
		mqMessage.correlationId = this.parametrosCliente.getGrupoMensaje().getBytes();
		mqMessage.groupId = new String(this.parametrosCliente.getNumeroTransaccion() + aleatorio).getBytes();
		mqMessage.messageSequenceNumber = new Integer(this.parametrosCliente.getGrupoMensaje()).intValue();
	}


}
